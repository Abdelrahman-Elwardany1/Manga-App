package com.example.mangaapp.features.chapters.data

import com.example.mangaapp.features.chapters.domain.ChapterRepository
import com.example.mangaapp.features.chapters.domain.models.Chapter
import com.example.mangaapp.features.search.data.api.MangaApiClient

class ChapterRepositoryImpl : ChapterRepository {
    override suspend fun getChapters(mangaId: String): Result<List<Chapter>> {
        return try {
            val response = MangaApiClient.apiService.getChapters(mangaId = mangaId)
            val chapters = response.data.map { chapterData ->
                val attributes = chapterData.attributes
                Chapter(
                    id = chapterData.id,
                    title = attributes.title ?: "No Title",
                    chapterNumber = attributes.chapter ?: "N/A",
                    pages = attributes.pages ?: 0,
                    language = attributes.translatedLanguage ?: "Unknown"
                )
            }
            Result.success(chapters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}