package com.ones.assistant.utilities

import com.ones.assistant.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserStateManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    fun setUser(user: User) {
        _currentUser.value = user
    }

    fun setToken(token: String?) {
        _token.value = token
    }

    fun clearUser() {
        _currentUser.value = null
        _token.value = null
    }

    fun getUser(): User? = _currentUser.value

    fun getToken(): String? = _token.value
}
