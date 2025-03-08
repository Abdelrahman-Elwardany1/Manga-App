package com.example.mangaapp.features.search.data.api

import com.example.mangaapp.features.chapterreader.data.models.ChapterReaderResponse
import com.example.mangaapp.features.chapters.data.models.ChapterResponse
import com.example.mangaapp.features.details.data.models.MangaDetailsResponse
import com.example.mangaapp.features.search.data.models.MangaResponse
import com.example.mangaapp.features.search.data.models.TagResponse
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("manga/{id}")
    suspend fun getMangaDetails(
        @Path("id") mangaId: String,
        @Query("includes[]") includes: List<String> = listOf("cover_art")
    ): MangaDetailsResponse

    @GET("chapter")
    suspend fun getChapters(
        @Query("manga") mangaId: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("translatedLanguage[]") translatedLanguage: List<String> = listOf("en"),
        @Query("order[chapter]") order: String = "desc"
    ): ChapterResponse

    @GET("at-home/server/{chapterId}")
    suspend fun getChapterPages(
        @Path("chapterId") chapterId: String
    ): ChapterReaderResponse
}