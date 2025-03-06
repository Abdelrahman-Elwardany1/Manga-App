package com.example.mangaapp.features.search.data

import com.example.mangaapp.features.search.data.api.MangaApiClient
import com.example.mangaapp.features.search.domain.MangaRepository
import com.example.mangaapp.features.search.domain.models.Manga

class MangaRepositoryImpl : MangaRepository {

    private val genreTagMap = mapOf(
        "Action" to "391b0423-d847-456f-aff0-8b0cfc03066b",
        "Adventure" to "87cc87cd-a395-47af-b27a-93258283bbc6",
        "Comedy" to "4d32cc48-9f00-4cca-9b5a-a839f0764984",
        "Drama" to "b9af3a63-f058-46de-a9a0-e0c13906197a",
        "Fantasy" to "cdc58593-87dd-415e-bbc0-2ec27bf404cc"
    )

    override suspend fun getMangaList(
        title: String?,
        genre: String?,
        offset: Int
    ): Result<List<Manga>> {
        return try {
            val safeTitle = title?.takeIf { it.isNotBlank() }

            val tagIds = genre?.let { friendlyName ->
                genreTagMap[friendlyName]?.let { tagId ->
                    listOf(tagId)
                }
            } ?: emptyList()

            val response = MangaApiClient.apiService.getMangaList(
                title = safeTitle,
                offset = offset,
                includedTags = tagIds
            )
            val mangaList = response.data.map { mangaData ->
                val attributes = mangaData.attributes
                val mangaTitle = attributes.title?.get("en") ?: "No Title"
                val mangaDescription = attributes.description?.get("en") ?: "No Description"
                val coverUrl = mangaData.relationships.firstOrNull { it.type == "cover_art" }
                    ?.attributes?.fileName?.let { fileName ->
                        "https://uploads.mangadex.org/covers/${mangaData.id}/$fileName"
                    } ?: ""
                Manga(
                    id = mangaData.id,
                    title = mangaTitle,
                    description = mangaDescription,
                    coverUrl = coverUrl
                )
            }
            Result.success(mangaList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}