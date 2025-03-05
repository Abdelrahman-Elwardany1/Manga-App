package com.example.mangaapp.features.details.data

import com.example.mangaapp.features.details.domain.DetailsRepository
import com.example.mangaapp.features.details.domain.models.MangaDetails
import com.example.mangaapp.features.search.data.api.MangaApiClient
import com.example.mangaapp.features.search.data.models.MangaData

class DetailsRepositoryImpl : DetailsRepository {
    override suspend fun getMangaDetails(mangaId: String): Result<MangaDetails> {
        return try {
            val response = MangaApiClient.apiService.getMangaDetails(mangaId)
            val data: MangaData = response.data

            val attributes = data.attributes
            val title = attributes.title?.get("en") ?: "No Title"
            val description = attributes.description?.get("en") ?: "No Description"
            val coverUrl = data.relationships.firstOrNull { it.type == "cover_art" }
                ?.attributes?.fileName?.let { fileName ->
                    "https://uploads.mangadex.org/covers/${data.id}/$fileName"
                } ?: ""

            Result.success(MangaDetails(
                id = data.id,
                title = title,
                description = description,
                coverUrl = coverUrl
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}