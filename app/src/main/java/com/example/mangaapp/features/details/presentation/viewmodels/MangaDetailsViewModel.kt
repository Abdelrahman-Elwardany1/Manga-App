package com.example.mangaapp.features.details.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.details.domain.models.MangaDetails
import com.example.mangaapp.features.details.domain.usecases.GetMangaDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MangaDetailsUiState(
    val manga: MangaDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MangaDetailsViewModel(private val getMangaDetailsUseCase: GetMangaDetailsUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(MangaDetailsUiState())
    val uiState: StateFlow<MangaDetailsUiState> = _uiState

    fun loadMangaDetails(mangaId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = getMangaDetailsUseCase(mangaId)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    manga = result.getOrNull(),
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