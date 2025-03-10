package com.example.mangaapp.features.home.domain

import com.example.mangaapp.features.search.domain.models.Manga

interface HomeMangaRepository {
    suspend fun getPopularMangaList(limit: Int = 20): Result<List<Manga>>
    suspend fun getRecentMangaList(limit: Int = 20): Result<List<Manga>>
    suspend fun getRelevantMangaList(limit: Int = 20): Result<List<Manga>>
}