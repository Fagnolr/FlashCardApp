package com.example.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.persistence.dao.FlashCardDao
import com.example.persistence.model.FlashCardEntity

@Database(
    entities = [
        FlashCardEntity::class,
    ],
    exportSchema = false,
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashCardDao(): FlashCardDao
}