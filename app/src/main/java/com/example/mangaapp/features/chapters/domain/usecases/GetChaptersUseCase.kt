package com.example.mangaapp.features.chapters.domain.usecases

import com.example.mangaapp.features.chapters.domain.ChapterRepository
import com.example.mangaapp.features.chapters.domain.models.Chapter

class GetChaptersUseCase(private val repository: ChapterRepository) {
    suspend operator fun invoke(mangaId: String): Result<List<Chapter>> {
        return repository.getChapters(mangaId)
    }
}