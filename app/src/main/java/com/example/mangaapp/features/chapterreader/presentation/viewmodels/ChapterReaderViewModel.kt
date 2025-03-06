package com.example.mangaapp.features.chapterreader.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.chapterreader.domain.models.ChapterPage
import com.example.mangaapp.features.chapterreader.domain.usecases.GetChapterPagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChapterReaderUiState(
    val pages: List<ChapterPage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChapterReaderViewModel(private val getChapterPagesUseCase: GetChapterPagesUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ChapterReaderUiState())
    val uiState: StateFlow<ChapterReaderUiState> = _uiState

    fun loadChapterPages(chapterId: String) {
        viewModelScope.launch {
            _uiState.value = ChapterReaderUiState(isLoading = true)
            val result = getChapterPagesUseCase(chapterId)
            if (result.isSuccess) {
                _uiState.value = ChapterReaderUiState(
                    pages = result.getOrDefault(emptyList()),
                    isLoading = false
                )
            } else {
                _uiState.value = ChapterReaderUiState(
                    error = result.exceptionOrNull()?.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }
}