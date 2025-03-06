package com.example.mangaapp.features.chapterreader.domain.usecases

import com.example.mangaapp.features.chapterreader.domain.ChapterReaderRepository
import com.example.mangaapp.features.chapterreader.domain.models.ChapterPage

class GetChapterPagesUseCase(private val repository: ChapterReaderRepository) {
    suspend operator fun invoke(chapterId: String): Result<List<ChapterPage>> {
        return repository.getChapterPages(chapterId)
    }
}