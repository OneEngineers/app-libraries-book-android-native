package com.assistant.libraries.data.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val phoneNumber: String? = null,
    val profileImageUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val createdAt: String,
    val updatedAt: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String? = null
)

data class AuthResponse(
    val user: User,
    val token: String,
    val refreshToken: String
)

sealed class AuthResult {
    data class Success(val authResponse: AuthResponse) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}
