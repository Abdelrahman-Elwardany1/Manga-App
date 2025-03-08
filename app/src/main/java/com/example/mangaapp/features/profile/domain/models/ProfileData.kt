package com.example.mangaapp.features.profile.domain.models

data class ProfileData(
    val firstName: String,
    val lastName: String,
    val nickName: String,
    val profilePicUrl: String? = null
)