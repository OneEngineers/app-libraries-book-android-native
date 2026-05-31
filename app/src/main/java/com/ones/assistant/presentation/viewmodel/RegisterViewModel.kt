package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.data.model.AuthResult
import com.ones.assistant.data.model.RegisterRequest
import com.ones.assistant.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    
    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }
    
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }
    
    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone)
    }
    
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
    
    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }
    
    fun onPasswordVisibilityToggle() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }
    
    fun onConfirmPasswordVisibilityToggle() {
        _uiState.value = _uiState.value.copy(isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible)
    }
    
    fun register() {
        if (_uiState.value.isLoading) return
        
        val name = _uiState.value.name.trim()
        val email = _uiState.value.email.trim()
        val phone = _uiState.value.phoneNumber.trim()
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        
        // Validation
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please fill in all required fields")
            return
        }
        
        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match")
            return
        }
        
        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
            return
        }
        
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        
        viewModelScope.launch {
            val result = authRepository.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    name = name,
                    phoneNumber = if (phone.isBlank()) null else phone
                )
            )
            
            _uiState.value = when (result) {
                is AuthResult.Success -> {
                    com.ones.assistant.utilities.UserStateManager.setUser(result.authResponse.user)
                    _uiState.value.copy(
                        isLoading = false,
                        isRegistrationSuccessful = true,
                        errorMessage = null
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is AuthResult.Loading -> {
                    _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun resetRegistrationState() {
        _uiState.value = _uiState.value.copy(isRegistrationSuccessful = false)
    }
}

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val errorMessage: String? = null
)
