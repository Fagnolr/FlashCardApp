package com.example.persistence.di

import android.content.Context
import androidx.room.Room
import com.example.persistence.AppDatabase
import com.example.persistence.dao.FlashCardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "room-database",
    ).build()

    @Provides
    fun providesFlashCardDao(database: AppDatabase): FlashCardDao = database.flashCardDao()
}