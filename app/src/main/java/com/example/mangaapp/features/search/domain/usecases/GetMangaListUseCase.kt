package com.example.mangaapp.features.search.domain.usecases

import com.example.mangaapp.features.search.domain.MangaRepository
import com.example.mangaapp.features.search.domain.models.Manga

class GetMangaListUseCase(private val repository: MangaRepository) {
    suspend operator fun invoke(title: String? = null, genre: String? = null): Result<List<Manga>> {
        return repository.getMangaList(title, genre)
    }
}