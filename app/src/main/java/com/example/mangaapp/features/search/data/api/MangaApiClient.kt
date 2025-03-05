package com.example.mangaapp.features.search.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MangaApiClient {
    val apiService: MangaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.mangadex.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MangaApiService::class.java)
    }
}