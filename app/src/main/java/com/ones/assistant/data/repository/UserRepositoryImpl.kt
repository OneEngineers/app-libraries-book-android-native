package com.ones.assistant.data.repository

import com.ones.assistant.data.model.User
import com.ones.assistant.domain.repositories.user.UserRepository
import com.ones.assistant.graphql.auth.UpdateProfileMutation
import com.ones.assistant.graphql.auth.GetProfileUserQuery
import com.ones.assistant.graphql.auth.UploadFileMutation
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.DefaultUpload
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.FileSystem
import okio.Path.Companion.toPath
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apolloClientAuth: ApolloClient
) : UserRepository {

    override suspend fun uploadFile(filePath: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(filePath)
                if (!file.exists()) {
                    return@withContext Result.failure(Exception("File not found at $filePath"))
                }

                // Using Builder which is the public API
                val upload = DefaultUpload.Builder()
                    .content { sink: BufferedSink ->
                        FileSystem.SYSTEM.source(file.absolutePath.toPath()).use { source ->
                            sink.writeAll(source)
                        }
                    }
                    .contentType("image/*")
                    .contentLength(file.length())
                    .fileName(file.name)
                    .build()

                val mutation = UploadFileMutation(file = upload)
                val response = apolloClientAuth.mutation(mutation).execute()

                if (response.hasErrors()) {
                    Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Upload failed"))
                } else {
                    response.data?.uploadFile?.url?.let { Result.success(it) }
                        ?: Result.failure(Exception("No URL returned after upload"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateProfile(displayName: String?, imageProfile: String?): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val mutation = UpdateProfileMutation(
                    displayName = Optional.presentIfNotNull(displayName),
                    imageProfile = Optional.presentIfNotNull(imageProfile),
                )
                val response = apolloClientAuth.mutation(mutation).execute()

                if (response.hasErrors()) {
                    Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
                } else {
                    val profile = response.data?.updateProfile
                    if (profile != null) {
                        Result.success(
                            User(
                                id = profile.id,
                                email = "",
                                name = profile.displayName,
                                profileImageUrl = profile.imageProfile,
                                createdAt = "",
                                updatedAt = ""
                            )
                        )
                    } else {
                        Result.failure(Exception("Failed to update profile"))
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserProfile(id: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val query = GetProfileUserQuery(id)
                val response = apolloClientAuth.query(query).execute()

                if (response.hasErrors()) {
                    Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
                } else {
                    val profile = response.data?.getProfileUser
                    if (profile != null) {
                        Result.success(
                            User(
                                id = profile.id,
                                email = "",
                                name = profile.displayName,
                                profileImageUrl = profile.imageProfile,
                                createdAt = "",
                                updatedAt = ""
                            )
                        )
                    } else {
                        Result.failure(Exception("Profile not found"))
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
