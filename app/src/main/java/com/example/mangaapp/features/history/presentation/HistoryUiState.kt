package com.example.mangaapp.features.history.presentation

import com.example.mangaapp.features.search.domain.models.Manga

data class HistoryUiState(
    val mangaList: List<Manga> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)