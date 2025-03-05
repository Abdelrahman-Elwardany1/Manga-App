package com.example.mangaapp.features.details.domain.usecases

import com.example.mangaapp.features.details.domain.DetailsRepository
import com.example.mangaapp.features.details.domain.models.MangaDetails

class GetMangaDetailsUseCase(private val repository: DetailsRepository) {
    suspend operator fun invoke(mangaId: String): Result<MangaDetails> {
        return repository.getMangaDetails(mangaId)
    }
}