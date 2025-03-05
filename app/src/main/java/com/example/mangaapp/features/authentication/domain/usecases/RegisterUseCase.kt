package com.example.mangaapp.features.authentication.domain.usecases

import com.example.mangaapp.features.authentication.domain.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.register(email, password)
    }
}