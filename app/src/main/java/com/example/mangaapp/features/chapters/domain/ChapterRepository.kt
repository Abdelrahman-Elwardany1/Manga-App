package com.example.mangaapp.features.chapters.domain

import com.example.mangaapp.features.chapters.domain.models.Chapter

interface ChapterRepository {
    suspend fun getChapters(mangaId: String): Result<List<Chapter>>
}