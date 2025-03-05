package com.example.mangaapp.features.search.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.search.domain.models.Tag
import com.example.mangaapp.features.search.domain.usecases.GetTagListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TagUiState {
    object Loading : TagUiState()
    data class Success(val tags: List<Tag>) : TagUiState()
    data class Error(val message: String) : TagUiState()
}

class TagViewModel(private val getTagListUseCase: GetTagListUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<TagUiState>(TagUiState.Loading)
    val uiState: StateFlow<TagUiState> = _uiState

    init {
        loadTags()
    }

    fun loadTags() {
        viewModelScope.launch {
            _uiState.value = TagUiState.Loading
            val result = getTagListUseCase()
            if (result.isSuccess) {
                _uiState.value = TagUiState.Success(result.getOrDefault(emptyList()))
            } else {
                _uiState.value =
                    TagUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}