package com.example.mangaapp.features.history.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyItem: HistoryItemEntity)

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    suspend fun getHistory(): List<HistoryItemEntity>

    @Query("DELETE FROM history")
    suspend fun clearHistory()
}