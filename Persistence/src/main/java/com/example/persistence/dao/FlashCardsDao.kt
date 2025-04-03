package com.example.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.persistence.model.FlashCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashCardDao {
    @Query("SELECT * FROM FlashCardEntity")
    fun getAll(): Flow<List<FlashCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flashcard: FlashCardEntity)

    @Delete
    suspend fun delete(flashcard: FlashCardEntity)
}
