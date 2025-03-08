package com.example.mangaapp.features.profile.presentation.models

import com.example.mangaapp.features.profile.domain.models.ProfileData

data class ProfileUiState(
    val profileData: ProfileData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)