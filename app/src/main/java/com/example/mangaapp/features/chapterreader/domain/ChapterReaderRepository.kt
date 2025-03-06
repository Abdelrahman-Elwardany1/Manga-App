package com.example.mangaapp.features.chapterreader.domain

import com.example.mangaapp.features.chapterreader.domain.models.ChapterPage

interface ChapterReaderRepository {
    suspend fun getChapterPages(chapterId: String): Result<List<ChapterPage>>
}