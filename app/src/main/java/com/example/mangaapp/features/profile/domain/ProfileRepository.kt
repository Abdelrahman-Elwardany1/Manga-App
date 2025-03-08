package com.example.mangaapp.features.profile.domain

import com.example.mangaapp.features.profile.domain.models.ProfileData

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Result<ProfileData>
    suspend fun updateProfilePic(userId: String, imageUrl: String): Result<Unit>
}