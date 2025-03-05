package com.example.mangaapp.features.search.domain

import com.example.mangaapp.features.search.domain.models.Manga

interface MangaRepository {
    suspend fun getMangaList(title: String? = null, genre: String? = null): Result<List<Manga>>
}