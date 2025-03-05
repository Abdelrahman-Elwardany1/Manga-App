package com.example.mangaapp.features.search.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.search.domain.models.Manga
import com.example.mangaapp.features.search.domain.usecases.GetMangaListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val mangaList: List<Manga> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedGenre: String = ""
)

class SearchViewModel(private val getMangaListUseCase: GetMangaListUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        loadManga()
    }

    fun loadManga(title: String? = null, genre: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val safeTitle = title?.takeIf { it.isNotBlank() }
            val result = getMangaListUseCase(safeTitle, genre)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    mangaList = result.getOrDefault(emptyList()),
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

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onGenreSelected(genre: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedGenre = genre
        )
        loadManga(title = null, genre = genre)
    }

    fun onSearch() {
        loadManga(title = _uiState.value.searchQuery, genre = _uiState.value.selectedGenre)
    }

}