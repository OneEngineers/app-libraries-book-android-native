package com.ones.assistant.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

fun resolveProfileImageModel(profileImageUrl: String?): Any? {
    val value = profileImageUrl?.trim().orEmpty()
    if (value.isEmpty()) return null
    return when {
        value.startsWith("http", ignoreCase = true) -> value
        value.startsWith("data:image", ignoreCase = true) -> value
        else -> "data:image/*;base64,$value"
    }
}

@Composable
fun ProfileAvatar(
    profileImageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    contentDescription: String = "Profile",
    fallbackIcon: ImageVector = Icons.Default.AccountCircle
) {
    val model = remember(profileImageUrl) { resolveProfileImageModel(profileImageUrl) }

    if (model == null) {
        Icon(
            imageVector = fallbackIcon,
            contentDescription = contentDescription,
            modifier = modifier.size(size)
        )
    } else {
        Image(
            painter = rememberAsyncImagePainter(model = model),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(CircleShape)
        )
    }
}
