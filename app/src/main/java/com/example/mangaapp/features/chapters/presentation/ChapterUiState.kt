package com.example.mangaapp.features.chapters.presentation

import com.example.mangaapp.features.chapters.domain.models.Chapter

data class ChapterUiState(
    val chapters: List<Chapter> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)