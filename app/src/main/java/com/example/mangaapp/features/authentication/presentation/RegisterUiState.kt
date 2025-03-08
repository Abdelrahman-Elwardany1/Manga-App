package com.example.mangaapp.features.authentication.presentation

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val nickName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)