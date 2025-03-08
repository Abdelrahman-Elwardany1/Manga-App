package com.example.mangaapp.features.history.domain

import com.example.mangaapp.features.history.domain.models.HistoryItem

interface HistoryRepository {
    suspend fun insertHistory(mangaId: String)
    suspend fun getHistory(): List<HistoryItem>
    suspend fun clearHistory()
}