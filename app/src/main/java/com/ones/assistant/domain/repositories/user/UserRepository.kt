package com.ones.assistant.domain.repositories.user

import com.ones.assistant.data.model.User

interface UserRepository {
    suspend fun updateProfile(displayName: String?, imageProfile: String?): Result<User>
    suspend fun getUserProfile(id: String): Result<User>
    suspend fun uploadFile(filePath: String): Result<String>
}
