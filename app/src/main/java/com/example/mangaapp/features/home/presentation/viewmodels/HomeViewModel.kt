package com.example.mangaapp.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.home.data.HomeMangaRepositoryImpl
import com.example.mangaapp.features.home.domain.HomeMangaRepository
import com.example.mangaapp.features.search.domain.models.Manga
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeMangaRepository = HomeMangaRepositoryImpl()
) : ViewModel() {

    private val _popularManga = MutableStateFlow<List<Manga>>(emptyList())
    val popularManga: StateFlow<List<Manga>> = _popularManga

    private val _recentManga = MutableStateFlow<List<Manga>>(emptyList())
    val recentManga: StateFlow<List<Manga>> = _recentManga

    private val _relevantManga = MutableStateFlow<List<Manga>>(emptyList())
    val relevantManga: StateFlow<List<Manga>> = _relevantManga

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadMangaSections()
    }

    fun loadMangaSections() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val popularResult = repository.getPopularMangaList()
                val recentResult = repository.getRecentMangaList()
                val comingSoonResult = repository.getRelevantMangaList()

                if (popularResult.isSuccess) {
                    _popularManga.value = popularResult.getOrDefault(emptyList())
                } else {
                    _error.value = popularResult.exceptionOrNull()?.message
                }

                if (recentResult.isSuccess) {
                    _recentManga.value = recentResult.getOrDefault(emptyList())
                } else {
                    _error.value = recentResult.exceptionOrNull()?.message
                }

                if (comingSoonResult.isSuccess) {
                    _relevantManga.value = comingSoonResult.getOrDefault(emptyList())
                } else {
                    _error.value = comingSoonResult.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}