package com.example.mangaapp.features.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.authentication.domain.usecases.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    private val passwordRegex = "^(?=.*[A-Z]).{8,}$".toRegex()

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(
            email = newEmail,
            error = if (newEmail.matches(emailRegex)) null else "Invalid email format"
        )
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(
            password = newPassword,
            error = if (newPassword.matches(passwordRegex)) null else "Password must be 8+ chars with 1 uppercase"
        )
    }

    fun register() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = registerUseCase(_uiState.value.email, _uiState.value.password)
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