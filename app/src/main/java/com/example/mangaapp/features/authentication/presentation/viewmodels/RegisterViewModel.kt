package com.example.mangaapp.features.authentication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.authentication.data.UserProfileRepositoryImpl
import com.example.mangaapp.features.authentication.domain.UserProfileRepository
import com.example.mangaapp.features.authentication.domain.usecases.RegisterUseCase
import com.example.mangaapp.features.authentication.presentation.RegisterUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val userProfileRepository: UserProfileRepository = UserProfileRepositoryImpl()
) : ViewModel() {

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
        val regexError = if (!newPassword.matches(passwordRegex))
            "Password must be at least 8 characters with 1 uppercase letter" else null

        _uiState.value = _uiState.value.copy(
            password = newPassword,
            error = regexError
        )

        if (_uiState.value.confirmPassword.isNotEmpty()) {
            if (regexError == null) {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newConfirmPassword)

        if (newConfirmPassword.isNotEmpty()) {
            if (_uiState.value.password != newConfirmPassword) {
                _uiState.value = _uiState.value.copy(error = "")
            } else if (_uiState.value.password.matches(passwordRegex)) {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    fun onFirstNameChange(newFirstName: String) {
        _uiState.value = _uiState.value.copy(firstName = newFirstName)
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.value = _uiState.value.copy(lastName = newLastName)
    }

    fun onNickNameChange(newNickName: String) {
        _uiState.value = _uiState.value.copy(nickName = newNickName)
    }

    fun register() {
        /*if (_uiState.value.confirmPassword.isNotEmpty() && _uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.value = _uiState.value.copy(error = "Passwords do not match")
            return
        }*/

        if (!_uiState.value.password.matches(passwordRegex)) {
            _uiState.value = _uiState.value.copy(error = "Password must be at least 8 characters with 1 uppercase letter")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = registerUseCase(_uiState.value.email, _uiState.value.password)
            if (result.isSuccess) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val profileResult = userProfileRepository.createUserProfile(
                        userId = userId,
                        firstName = _uiState.value.firstName,
                        lastName = _uiState.value.lastName,
                        nickName = _uiState.value.nickName
                    )
                    if (profileResult.isSuccess) {
                        _uiState.value = _uiState.value.copy(success = true, isLoading = false)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            error = profileResult.exceptionOrNull()?.message ?: "Profile save error",
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(error = "User ID not found", isLoading = false)
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message ?: "Registration failed",
                    isLoading = false
                )
            }
        }
    }
}