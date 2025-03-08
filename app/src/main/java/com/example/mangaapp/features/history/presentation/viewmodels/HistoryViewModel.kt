package com.example.mangaapp.features.history.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.history.domain.HistoryRepository
import com.example.mangaapp.features.history.domain.models.HistoryItem
import com.example.mangaapp.features.history.domain.usecases.GetMangaDetailsForHistoryUseCase
import com.example.mangaapp.features.history.presentation.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
    private val getMangaDetailsForHistoryUseCase: GetMangaDetailsForHistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    fun insertHistory(mangaId: String) {
        viewModelScope.launch {
            historyRepository.insertHistory(mangaId)
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val historyItems: List<HistoryItem> = historyRepository.getHistory()
                val mangaIds = historyItems.map { it.mangaId }
                val result = getMangaDetailsForHistoryUseCase(mangaIds)

                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        mangaList = result.getOrDefault(emptyList()),
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = result.exceptionOrNull()?.message ?: "Failed to load history",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }
}