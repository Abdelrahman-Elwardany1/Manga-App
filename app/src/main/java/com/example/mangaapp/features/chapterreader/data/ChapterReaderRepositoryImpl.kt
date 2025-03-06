package com.example.mangaapp.features.chapterreader.data

import com.example.mangaapp.features.chapterreader.domain.ChapterReaderRepository
import com.example.mangaapp.features.chapterreader.domain.models.ChapterPage
import com.example.mangaapp.features.search.data.api.MangaApiClient

class ChapterReaderRepositoryImpl : ChapterReaderRepository {
    override suspend fun getChapterPages(chapterId: String): Result<List<ChapterPage>> {
        return try {
            val response = MangaApiClient.apiService.getChapterPages(chapterId)
            val pages = response.chapter.data.map { filename ->
                "${response.baseUrl}/data/${response.chapter.hash}/$filename"
            }
            Result.success(pages.map { ChapterPage(it) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}