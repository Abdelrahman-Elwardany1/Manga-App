package com.example.mangaapp.features.history.domain.usecases

import com.example.mangaapp.features.details.domain.DetailsRepository
import com.example.mangaapp.features.details.domain.models.MangaDetails
import com.example.mangaapp.features.search.domain.models.Manga
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetMangaDetailsForHistoryUseCase(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(mangaIds: List<String>): Result<List<Manga>> {
        return try {
            val detailsList = coroutineScope {
                mangaIds.map { id ->
                    async { detailsRepository.getMangaDetails(id) }
                }.awaitAll().mapNotNull { it.getOrNull() }
            }
            val mangaList = detailsList.map { it.toManga() }
            Result.success(mangaList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

fun MangaDetails.toManga(): Manga {
    return Manga(
        id = this.id,
        title = this.title,
        description = this.description,
        coverUrl = this.coverUrl
    )
}