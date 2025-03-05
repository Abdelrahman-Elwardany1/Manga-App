package com.example.mangaapp.features.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.authentication.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = loginUseCase(_uiState.value.email, _uiState.value.password)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(success = true, isLoading = false)
            } else {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }
}