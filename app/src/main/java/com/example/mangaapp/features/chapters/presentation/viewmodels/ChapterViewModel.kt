package com.example.mangaapp.features.chapters.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.chapters.domain.usecases.GetChaptersUseCase
import com.example.mangaapp.features.chapters.presentation.ChapterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChapterViewModel(private val getChaptersUseCase: GetChaptersUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ChapterUiState())
    val uiState: StateFlow<ChapterUiState> = _uiState

    fun loadChapters(mangaId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = getChaptersUseCase(mangaId)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    chapters = result.getOrDefault(emptyList()),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }
}