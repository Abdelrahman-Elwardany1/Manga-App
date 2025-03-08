package com.example.mangaapp.features.history.data

import com.example.mangaapp.features.history.data.local.HistoryDao
import com.example.mangaapp.features.history.data.local.HistoryItemEntity
import com.example.mangaapp.features.history.domain.HistoryRepository
import com.example.mangaapp.features.history.domain.models.HistoryItem

class HistoryRepositoryImpl(private val dao: HistoryDao) : HistoryRepository {

    override suspend fun insertHistory(mangaId: String) {
        val entity = HistoryItemEntity(mangaId = mangaId, timestamp = System.currentTimeMillis())

        dao.insert(entity)
    }

    override suspend fun getHistory(): List<HistoryItem> {
        return dao.getHistory().map { entity ->
            HistoryItem(mangaId = entity.mangaId, timestamp = entity.timestamp)
        }
    }

    override suspend fun clearHistory() {
        dao.clearHistory()
    }

}