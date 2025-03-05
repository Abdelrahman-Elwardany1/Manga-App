package com.example.mangaapp.features.details.domain

import com.example.mangaapp.features.details.domain.models.MangaDetails

interface DetailsRepository {
    suspend fun getMangaDetails(mangaId: String): Result<MangaDetails>
}