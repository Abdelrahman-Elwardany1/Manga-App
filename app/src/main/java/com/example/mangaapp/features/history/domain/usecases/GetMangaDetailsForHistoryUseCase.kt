package com.example.mangaapp.features.history.domain.usecases

import com.example.mangaapp.features.details.domain.DetailsRepository
import com.example.mangaapp.features.details.domain.models.MangaDetails
import com.example.mangaapp.features.search.domain.models.Manga
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class GetMangaDetailsForHistoryUseCase(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(mangaIds: List<String>, chunkSize: Int = 5, delayMillis: Long = 1000L): Result<List<Manga>> {
        return try {
            val mangaList = mutableListOf<Manga>()
            val chunks = mangaIds.chunked(chunkSize)
            for (chunk in chunks) {
                val detailsChunk = coroutineScope {
                    chunk.map { id ->
                        async { detailsRepository.getMangaDetails(id).getOrNull() }
                    }.awaitAll().filterNotNull()
                }
                mangaList.addAll(detailsChunk.map { it.toManga() })
                delay(delayMillis)
            }
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