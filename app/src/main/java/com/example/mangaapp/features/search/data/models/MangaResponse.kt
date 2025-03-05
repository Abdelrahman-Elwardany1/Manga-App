package com.example.mangaapp.features.search.data.models

data class MangaResponse(
    val result: String,
    val data: List<MangaData>
)