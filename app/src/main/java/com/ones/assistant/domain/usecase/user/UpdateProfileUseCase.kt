package com.ones.assistant.domain.usecase.user

import com.ones.assistant.data.model.User
import com.ones.assistant.domain.repositories.user.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(displayName: String?, imageProfile: String?): Result<User> {
        return userRepository.updateProfile(displayName, imageProfile)
    }
}
