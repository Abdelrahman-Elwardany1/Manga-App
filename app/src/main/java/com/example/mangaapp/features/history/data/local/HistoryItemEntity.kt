package com.example.mangaapp.features.history.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryItemEntity(
    @PrimaryKey val mangaId: String,
    val timestamp: Long
)