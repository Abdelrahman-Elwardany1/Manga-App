package com.example.mangaapp.features.chapterreader.data.models

data class ChapterReaderResponse(
    val result: String,
    val baseUrl: String,
    val chapter: ChapterAtHome
)