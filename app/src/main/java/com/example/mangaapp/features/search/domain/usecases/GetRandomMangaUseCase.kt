package com.example.mangaapp.features.search.domain.usecases

import com.example.mangaapp.features.search.domain.MangaRepository
import com.example.mangaapp.features.search.domain.models.Manga

class GetRandomMangaUseCase(private val repository: MangaRepository) {

    suspend operator fun invoke(limit: Int = 20): Result<List<Manga>> {
        return try {
            val fetchLimit = 100
            val result = repository.getMangaList(
                title = null,
                genre = null,
                offset = 0,
                limit = fetchLimit,
                order = emptyMap()
            )
            if (result.isSuccess) {
                val randomList = result.getOrDefault(emptyList()).shuffled().take(limit)
                Result.success(randomList)
            } else {
                result as Result<List<Manga>>
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}