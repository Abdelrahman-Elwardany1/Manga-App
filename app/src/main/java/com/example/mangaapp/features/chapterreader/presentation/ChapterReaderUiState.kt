package com.example.mangaapp.features.chapterreader.presentation

import com.example.mangaapp.features.chapterreader.domain.models.ChapterPage

data class ChapterReaderUiState(
    val pages: List<ChapterPage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)