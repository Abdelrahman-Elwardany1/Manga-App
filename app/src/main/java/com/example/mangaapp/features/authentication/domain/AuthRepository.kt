package com.example.mangaapp.features.authentication.domain

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
}