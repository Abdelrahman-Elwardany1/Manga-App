package com.example.mangaapp.features.history.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import retrofit2.http.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyItem: HistoryItemEntity)

    @androidx.room.Query("SELECT * FROM history ORDER BY timestamp DESC")
    suspend fun getHistory(): List<HistoryItemEntity>

    @androidx.room.Query("DELETE FROM history")
    suspend fun clearHistory()
}