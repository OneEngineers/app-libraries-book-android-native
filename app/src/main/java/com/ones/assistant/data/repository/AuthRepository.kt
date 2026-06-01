package com.ones.assistant.data.repository

import com.ones.assistant.data.model.AuthResult
import com.ones.assistant.data.model.AuthResponse
import com.ones.assistant.data.model.User
import com.ones.assistant.data.model.LoginRequest
import com.ones.assistant.data.model.RegisterRequest
import com.ones.assistant.graphql.auth.LoginMutation
import com.ones.assistant.graphql.auth.RegisterMutation
import com.ones.assistant.graphql.auth.UpdateProfileMutation
import com.ones.assistant.utilities.UserStateManager
import com.ones.assistant.utilities.apolloClientAuth
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    
    suspend fun login(loginRequest: LoginRequest): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                if (loginRequest.email.isBlank() || loginRequest.password.isBlank()) {
                    AuthResult.Error("Email and password are required")
                } else if (!isValidEmail(loginRequest.email)) {
                    AuthResult.Error("Please enter a valid email address")
                } else if (loginRequest.password.length < 6) {
                    AuthResult.Error("Password must be at least 6 characters")
                } else {
                    val mutation = LoginMutation(email = loginRequest.email, password = loginRequest.password)
                    val response = apolloClientAuth.mutation(mutation).execute()
                    
                    if (response.hasErrors()) {
                        val errMsg = response.errors?.firstOrNull()?.message ?: "Unknown error"
                        AuthResult.Error(errMsg)
                    } else {
                        val token = response.data?.login?.token
                        if (token != null) {
                            val mockUser = User(
                                id = "user_sso",
                                email = loginRequest.email,
                                name = loginRequest.email.substringBefore("@"),
                                phoneNumber = null,
                                isEmailVerified = true,
                                createdAt = "2026-05-26T00:00:00Z",
                                updatedAt = "2026-05-26T00:00:00Z"
                            )
                            val authResponse = AuthResponse(
                                user = mockUser,
                                token = token,
                                refreshToken = ""
                            )
                            AuthResult.Success(authResponse)
                        } else {
                            AuthResult.Error("No token received from authentication server")
                        }
                    }
                }
            } catch (e: Exception) {
                AuthResult.Error("Login failed: ${e.message}")
            }
        }
    }
    
    suspend fun register(registerRequest: RegisterRequest): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                if (registerRequest.email.isBlank() || registerRequest.password.isBlank() || registerRequest.name.isBlank()) {
                    AuthResult.Error("All fields are required")
                } else if (!isValidEmail(registerRequest.email)) {
                    AuthResult.Error("Please enter a valid email address")
                } else if (registerRequest.password.length < 6) {
                    AuthResult.Error("Password must be at least 6 characters")
                } else if (registerRequest.name.length < 2) {
                    AuthResult.Error("Name must be at least 2 characters")
                } else {
                    val mutation = RegisterMutation(
                        username = registerRequest.name,
                        email = registerRequest.email,
                        password = registerRequest.password
                    )
                    val response = apolloClientAuth.mutation(mutation).execute()
                    
                    if (response.hasErrors()) {
                        val errMsg = response.errors?.firstOrNull()?.message ?: "Unknown error"
                        AuthResult.Error(errMsg)
                    } else {
                        val token = response.data?.register?.token
                        if (token != null) {
                            val mockUser = User(
                                id = "user_sso",
                                email = registerRequest.email,
                                name = registerRequest.name,
                                phoneNumber = registerRequest.phoneNumber,
                                isEmailVerified = false,
                                createdAt = "2026-05-26T00:00:00Z",
                                updatedAt = "2026-05-26T00:00:00Z"
                            )
                            val authResponse = AuthResponse(
                                user = mockUser,
                                token = token,
                                refreshToken = ""
                            )
                            AuthResult.Success(authResponse)
                        } else {
                            AuthResult.Error("No token received from registration server")
                        }
                    }
                }
            } catch (e: Exception) {
                AuthResult.Error("Registration failed: ${e.message}")
            }
        }
    }

    suspend fun updateProfile(
        token: String?,
        displayName: String?,
        imageProfile: String?
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = token?.trim()
                if (authToken.isNullOrBlank()) {
                    return@withContext Result.failure(Exception("Your session has expired. Please login again."))
                }

                val mutation = UpdateProfileMutation(
                    displayName = Optional.presentIfNotNull(displayName),
                    imageProfile = Optional.presentIfNotNull(imageProfile)
                )
                val response = apolloClientAuth(authToken).mutation(mutation).execute()

                if (response.hasErrors()) {
                    val firstError = response.errors?.firstOrNull()?.message ?: "Unknown error"
                    val isImageTooLong = firstError.contains("image", ignoreCase = true) &&
                        firstError.contains("too long", ignoreCase = true)
                    if (isImageTooLong && !displayName.isNullOrBlank()) {
                        val retryMutation = UpdateProfileMutation(
                            displayName = Optional.present(displayName),
                            imageProfile = Optional.Absent
                        )
                        val retryResponse = apolloClientAuth(authToken).mutation(retryMutation).execute()
                        if (retryResponse.hasErrors()) {
                            Result.failure(Exception(retryResponse.errors?.firstOrNull()?.message ?: firstError))
                        } else {
                            val updated = retryResponse.data?.updateProfile
                                ?: return@withContext Result.failure(Exception("No data received"))
                            val mapped = mapUpdatedUser(updated)
                            UserStateManager.setUser(mapped)
                            Result.success(mapped)
                        }
                    } else {
                        Result.failure(Exception(firstError))
                    }
                } else {
                    val updated = response.data?.updateProfile
                        ?: return@withContext Result.failure(Exception("No data received"))
                    val mapped = mapUpdatedUser(updated)
                    UserStateManager.setUser(mapped)
                    Result.success(mapped)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun mapUpdatedUser(updated: UpdateProfileMutation.UpdateProfile): User {
        // Preserve fields we don't get from this endpoint (ex: email, timestamps).
        val current = UserStateManager.getUser()
        return User(
            id = updated.id,
            email = current?.email ?: "",
            name = updated.displayName ?: updated.username,
            phoneNumber = current?.phoneNumber,
            profileImageUrl = updated.imageProfile,
            isEmailVerified = current?.isEmailVerified ?: false,
            createdAt = current?.createdAt ?: "",
            updatedAt = current?.updatedAt ?: ""
        )
    }
}
