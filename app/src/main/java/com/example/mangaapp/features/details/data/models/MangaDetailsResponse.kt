package com.example.mangaapp.features.details.data.models

import com.example.mangaapp.features.search.data.models.MangaData

data class MangaDetailsResponse(
    val result: String,
    val data: MangaData
)