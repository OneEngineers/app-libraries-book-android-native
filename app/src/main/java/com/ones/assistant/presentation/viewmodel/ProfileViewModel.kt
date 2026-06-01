package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.data.repository.AuthRepository
import com.ones.assistant.data.model.User
import com.ones.assistant.utilities.UserStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val authRepository = AuthRepository()
    
    init {
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val user = UserStateManager.getUser()
            
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    errorMessage = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Profile not found"
                )
            }
        }
    }
    
    fun retry() {
        loadUserProfile()
    }

    fun updateProfile(token: String?, displayName: String?, imageProfile: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, updateErrorMessage = null)
            val result = authRepository.updateProfile(
                token = token ?: UserStateManager.getToken(),
                displayName = displayName,
                imageProfile = imageProfile
            )
            _uiState.value = result.fold(
                onSuccess = { user ->
                    _uiState.value.copy(
                        isUpdating = false,
                        user = user,
                        updateErrorMessage = null,
                        updateSuccessNonce = _uiState.value.updateSuccessNonce + 1
                    )
                },
                onFailure = { err ->
                    _uiState.value.copy(isUpdating = false, updateErrorMessage = err.message ?: "Update failed")
                }
            )
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val isUpdating: Boolean = false,
    val updateErrorMessage: String? = null,
    val updateSuccessNonce: Int = 0
)
