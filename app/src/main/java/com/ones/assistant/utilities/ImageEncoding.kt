package com.ones.assistant.utilities

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun uriToBase64(
    contentResolver: ContentResolver,
    uri: Uri
): String {
    val bounds = decodeImageBounds(contentResolver, uri)
        ?: throw IllegalStateException("Unable to read selected image")

    val maxDimension = 1024
    val sampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, maxDimension, maxDimension)
    val decodeOptions = BitmapFactory.Options().apply {
        inSampleSize = sampleSize
        inPreferredConfig = Bitmap.Config.ARGB_8888
    }

    val bitmap = decodeBitmap(contentResolver, uri, decodeOptions)
        ?: throw IllegalStateException("Unable to decode selected image")

    val compressed = compressBitmapForProfile(bitmap)
    bitmap.recycle()
    return Base64.encodeToString(compressed, Base64.NO_WRAP)
}

private fun decodeImageBounds(contentResolver: ContentResolver, uri: Uri): BitmapFactory.Options? {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

    val decodedFromStream = contentResolver.openInputStream(uri)?.use { input ->
        BitmapFactory.decodeStream(input, null, options)
        options.outWidth > 0 && options.outHeight > 0
    } ?: false
    if (decodedFromStream) return options

    val decodedFromFd = contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
        BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor, null, options)
        options.outWidth > 0 && options.outHeight > 0
    } ?: false

    return if (decodedFromFd) options else null
}

private fun decodeBitmap(
    contentResolver: ContentResolver,
    uri: Uri,
    options: BitmapFactory.Options
): Bitmap? {
    val fromStream = contentResolver.openInputStream(uri)?.use { input ->
        BitmapFactory.decodeStream(input, null, options)
    }
    if (fromStream != null) return fromStream

    return contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
        BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor, null, options)
    }
}

private fun calculateInSampleSize(
    width: Int,
    height: Int,
    reqWidth: Int,
    reqHeight: Int
): Int {
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        var halfHeight = height / 2
        var halfWidth = width / 2
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
            halfHeight /= 2
            halfWidth /= 2
        }
    }
    return inSampleSize.coerceAtLeast(1)
}

private fun compressBitmapForProfile(bitmap: Bitmap): ByteArray {
    // Keep payload relatively small while avoiding local hard failure.
    val maxBase64Chars = 12_000
    val output = ByteArrayOutputStream()
    var current = bitmap
    var shouldRecycleCurrent = false
    var quality = 80
    var best = ByteArray(0)
    var bestEstimatedBase64Length = Int.MAX_VALUE

    repeat(12) {
        output.reset()
        current.compress(Bitmap.CompressFormat.JPEG, quality, output)
        val estimatedBase64Length = ((output.size() + 2) / 3) * 4
        if (estimatedBase64Length < bestEstimatedBase64Length) {
            best = output.toByteArray()
            bestEstimatedBase64Length = estimatedBase64Length
        }
        if (estimatedBase64Length <= maxBase64Chars) {
            if (shouldRecycleCurrent) current.recycle()
            return output.toByteArray()
        }

        // Try lowering quality first; then reduce dimensions.
        if (quality > 35) {
            quality -= 10
        } else {
            val nextWidth = (current.width * 0.75f).toInt().coerceAtLeast(40)
            val nextHeight = (current.height * 0.75f).toInt().coerceAtLeast(40)
            if (nextWidth == current.width && nextHeight == current.height) {
                return@repeat
            }
            val scaled = Bitmap.createScaledBitmap(current, nextWidth, nextHeight, true)
            if (shouldRecycleCurrent) current.recycle()
            current = scaled
            shouldRecycleCurrent = true
            quality = 75
        }
    }

    if (shouldRecycleCurrent) current.recycle()
    if (best.isNotEmpty()) return best
    throw IllegalStateException("Unable to process selected image")
}

