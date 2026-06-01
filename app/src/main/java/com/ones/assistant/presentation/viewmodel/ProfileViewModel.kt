package com.ones.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ones.assistant.data.model.User
import com.ones.assistant.domain.repositories.user.UserRepository
import com.ones.assistant.domain.usecase.user.UpdateProfileUseCase
import com.ones.assistant.utilities.UserStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val user = UserStateManager.getUser()
            
            if (user != null) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        user = user,
                        errorMessage = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Profile not found"
                    )
                }
            }
        }
    }

    fun updateProfile(displayName: String?, imageProfile: String?, localImagePath: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            var imageUrl = imageProfile
            
            if (localImagePath != null) {
                val uploadResult = userRepository.uploadFile(localImagePath)
                if (uploadResult.isSuccess) {
                    imageUrl = uploadResult.getOrNull()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = uploadResult.exceptionOrNull()?.message ?: "Failed to upload image"
                        )
                    }
                    return@launch
                }
            }
            
            val result = updateProfileUseCase(displayName, imageUrl)
            
            result.fold(
                onSuccess = { updatedUser ->
                    // Update local state manager too if needed
                    UserStateManager.setUser(updatedUser)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = updatedUser,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to update profile"
                        )
                    }
                }
            )
        }
    }
    
    fun retry() {
        loadUserProfile()
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)
