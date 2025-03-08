package com.example.mangaapp.features.authentication.domain

interface UserProfileRepository {
    suspend fun createUserProfile(
        userId: String,
        firstName: String,
        lastName: String,
        nickName: String
    ): Result<Unit>
}