package com.propertymanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propertymanager.models.User
import com.propertymanager.models.UserRole
import com.propertymanager.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val currentUser: User? = null,
    val error: String? = null
)

class AuthViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val userId = repository.getCurrentUserId()
        if (userId != null) {
            viewModelScope.launch {
                repository.getUser(userId).fold(
                    onSuccess = { user ->
                        _authState.value = _authState.value.copy(
                            isAuthenticated = true,
                            currentUser = user
                        )
                    },
                    onFailure = {
                        _authState.value = _authState.value.copy(isAuthenticated = false)
                    }
                )
            }
        }
    }

    fun signUp(email: String, password: String, name: String, phone: String, role: UserRole) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)

            repository.signUp(email, password).fold(
                onSuccess = { userId ->
                    val user = User(
                        id = userId,
                        email = email,
                        name = name,
                        phone = phone,
                        role = role
                    )
                    repository.createUser(user).fold(
                        onSuccess = {
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                isAuthenticated = true,
                                currentUser = user
                            )
                        },
                        onFailure = { e ->
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = e.message
                            )
                        }
                    )
                },
                onFailure = { e ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)

            repository.signIn(email, password).fold(
                onSuccess = { userId ->
                    repository.getUser(userId).fold(
                        onSuccess = { user ->
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                isAuthenticated = true,
                                currentUser = user
                            )
                        },
                        onFailure = { e ->
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = e.message
                            )
                        }
                    )
                },
                onFailure = { e ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState()
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}
