package com.example.mangaapp.features.home.data

import com.example.mangaapp.features.home.domain.HomeMangaRepository
import com.example.mangaapp.features.search.data.api.MangaApiClient
import com.example.mangaapp.features.search.data.api.MangaApiService
import com.example.mangaapp.features.search.domain.models.Manga

class HomeMangaRepositoryImpl(
    private val apiService: MangaApiService = MangaApiClient.apiService
) : HomeMangaRepository {

    override suspend fun getPopularMangaList(limit: Int): Result<List<Manga>> {
        return try {
            val response = apiService.getMangaList(
                title = null,
                offset = 0,
                limit = limit,
                order = mapOf("order[rating]" to "desc"),
                includedTags = emptyList()
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

    override suspend fun getRecentMangaList(limit: Int): Result<List<Manga>> {
        return try {
            val response = apiService.getMangaList(
                title = null,
                offset = 0,
                limit = limit,
                order = mapOf("order[latestUploadedChapter]" to "desc"),
                includedTags = emptyList()
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

    override suspend fun getRelevantMangaList(limit: Int): Result<List<Manga>> {
        return try {
            val response = apiService.getMangaList(
                title = null,
                offset = 0,
                limit = limit,
                order = mapOf("order[relevance]" to "desc"),
                includedTags = emptyList()
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