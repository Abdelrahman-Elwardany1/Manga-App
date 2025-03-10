package com.example.mangaapp.features.home.domain.usecases

import com.example.mangaapp.features.home.domain.HomeMangaRepository
import com.example.mangaapp.features.search.domain.models.Manga

class GetPopularMangaUseCase(private val repository: HomeMangaRepository) {
    suspend operator fun invoke(limit: Int = 20): Result<List<Manga>> {
        return repository.getPopularMangaList(limit)
    }
}