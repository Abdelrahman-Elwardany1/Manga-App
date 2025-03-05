package com.example.mangaapp.features.search.data

import com.example.mangaapp.features.search.data.api.MangaApiClient
import com.example.mangaapp.features.search.domain.TagRepository
import com.example.mangaapp.features.search.domain.models.Tag

class TagRepositoryImpl : TagRepository {
    override suspend fun getTagList(): Result<List<Tag>> {
        return try {
            val response = MangaApiClient.apiService.getTagList()
            val tagList = response.data.map { tagData ->
                Tag(
                    id = tagData.id,
                    name = tagData.attributes.name["en"] ?: "No Name",
                    group = tagData.attributes.group
                )
            }
            Result.success(tagList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}