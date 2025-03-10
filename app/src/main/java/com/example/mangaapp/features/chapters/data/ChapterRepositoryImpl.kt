package com.example.mangaapp.features.chapters.data

import com.example.mangaapp.features.chapters.domain.ChapterRepository
import com.example.mangaapp.features.chapters.domain.models.Chapter
import com.example.mangaapp.features.search.data.api.MangaApiClient
import kotlinx.coroutines.delay

class ChapterRepositoryImpl : ChapterRepository {

    override suspend fun getChapters(mangaId: String): Result<List<Chapter>> {
        val limit = 100
        val allChapters = mutableListOf<Chapter>()
        var offset = 0

        return try {
            while (true) {
                delay(1000L)
                val response = MangaApiClient.apiService.getChapters(
                    mangaId = mangaId,
                    offset = offset,
                    limit = limit
                )
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
                allChapters.addAll(chapters)
                if (chapters.size < limit) break
                offset += limit
            }
            Result.success(allChapters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}