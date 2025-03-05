package com.example.mangaapp.features.chapters.domain.models

data class Chapter(
    val id: String,
    val title: String,
    val chapterNumber: String,
    val pages: Int,
    val language: String
)