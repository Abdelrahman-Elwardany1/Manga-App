package com.example.mangaapp.features.search.data.api

import com.example.mangaapp.features.search.data.models.MangaResponse
import com.example.mangaapp.features.search.data.models.TagResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MangaApiService {
    @GET("manga")
    suspend fun getMangaList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("title") title: String? = null,
        @Query("includes[]") includes: List<String> = listOf("cover_art"),
        @Query("includedTags[]") includedTags: List<String> = emptyList(),
        @Query("availableTranslatedLanguage[]") availableTranslatedLanguage: List<String> = listOf("en")
    ): MangaResponse

    @GET("manga/tag")
    suspend fun getTagList(): TagResponse
}