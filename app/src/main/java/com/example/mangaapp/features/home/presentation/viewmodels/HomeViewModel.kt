package com.example.mangaapp.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.search.data.MangaRepositoryImpl
import com.example.mangaapp.features.search.domain.MangaRepository
import com.example.mangaapp.features.search.domain.models.Manga
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MangaRepository = MangaRepositoryImpl()
) : ViewModel() {

    private val _popularManga = MutableStateFlow<List<Manga>>(emptyList())
    val popularManga: StateFlow<List<Manga>> = _popularManga

    private val _recentManga = MutableStateFlow<List<Manga>>(emptyList())
    val recentManga: StateFlow<List<Manga>> = _recentManga

    private val _comingSoonManga = MutableStateFlow<List<Manga>>(emptyList())
    val comingSoonManga: StateFlow<List<Manga>> = _comingSoonManga

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val popular = repository.getMangaList(offset = 0).getOrElse { emptyList() }
                val recent = repository.getMangaList(offset = 20).getOrElse { emptyList() }
                val comingSoon = repository.getMangaList(offset = 40).getOrElse { emptyList() }

                _popularManga.value = popular
                _recentManga.value = recent
                _comingSoonManga.value = comingSoon
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
}