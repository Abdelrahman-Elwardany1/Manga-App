package com.example.mangaapp.features.authentication.domain.usecases

import com.example.mangaapp.features.authentication.domain.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.login(email, password)
    }
}