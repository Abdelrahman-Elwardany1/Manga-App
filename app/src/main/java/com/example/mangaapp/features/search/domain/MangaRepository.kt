package com.example.mangaapp.features.search.domain

import com.example.mangaapp.features.search.domain.models.Manga

interface MangaRepository {
    suspend fun getMangaList(
        title: String? = null,
        genre: String? = null,
        offset: Int = 0,
        limit: Int = 20,
        order: Map<String, String>? = null
    ): Result<List<Manga>>
}