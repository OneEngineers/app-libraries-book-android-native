package com.assistant.libraries.data.repository

import com.assistant.libraries.data.model.AuthResult
import com.assistant.libraries.data.model.LoginRequest
import com.assistant.libraries.data.model.RegisterRequest
import kotlinx.coroutines.delay

class AuthRepository {
    
    // Simulate API calls with delays
    suspend fun login(loginRequest: LoginRequest): AuthResult {
        delay(2000) // Simulate network delay
        
        return try {
            // Simulate validation
            if (loginRequest.email.isBlank() || loginRequest.password.isBlank()) {
                AuthResult.Error("Email and password are required")
            } else if (!isValidEmail(loginRequest.email)) {
                AuthResult.Error("Please enter a valid email address")
            } else if (loginRequest.password.length < 6) {
                AuthResult.Error("Password must be at least 6 characters")
            } else {
                // Simulate successful login
                val mockUser = com.assistant.libraries.data.model.User(
                    id = "user_123",
                    email = loginRequest.email,
                    name = "John Doe",
                    phoneNumber = "+1234567890",
                    isEmailVerified = true,
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                )
                
                val mockAuthResponse = com.assistant.libraries.data.model.AuthResponse(
                    user = mockUser,
                    token = "mock_jwt_token_123",
                    refreshToken = "mock_refresh_token_123"
                )
                
                AuthResult.Success(mockAuthResponse)
            }
        } catch (e: Exception) {
            AuthResult.Error("Login failed: ${e.message}")
        }
    }
    
    suspend fun register(registerRequest: RegisterRequest): AuthResult {
        delay(2000) // Simulate network delay
        
        return try {
            // Simulate validation
            if (registerRequest.email.isBlank() || registerRequest.password.isBlank() || registerRequest.name.isBlank()) {
                AuthResult.Error("All fields are required")
            } else if (!isValidEmail(registerRequest.email)) {
                AuthResult.Error("Please enter a valid email address")
            } else if (registerRequest.password.length < 6) {
                AuthResult.Error("Password must be at least 6 characters")
            } else if (registerRequest.name.length < 2) {
                AuthResult.Error("Name must be at least 2 characters")
            } else {
                // Simulate successful registration
                val mockUser = com.assistant.libraries.data.model.User(
                    id = "user_${System.currentTimeMillis()}",
                    email = registerRequest.email,
                    name = registerRequest.name,
                    phoneNumber = registerRequest.phoneNumber,
                    isEmailVerified = false,
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                )
                
                val mockAuthResponse = com.assistant.libraries.data.model.AuthResponse(
                    user = mockUser,
                    token = "mock_jwt_token_${System.currentTimeMillis()}",
                    refreshToken = "mock_refresh_token_${System.currentTimeMillis()}"
                )
                
                AuthResult.Success(mockAuthResponse)
            }
        } catch (e: Exception) {
            AuthResult.Error("Registration failed: ${e.message}")
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
