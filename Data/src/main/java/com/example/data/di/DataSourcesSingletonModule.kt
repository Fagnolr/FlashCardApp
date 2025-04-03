package com.example.data.di

import com.example.data.data_source.local.FlashCardLocalDataSource
import com.example.data.data_source.local.FlashCardLocalDataSourceImpl
import com.example.data.repository.FlashCardsRepositoryImpl
import com.example.domain.repository.FlashCardsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourcesSingletonModule {

    @Binds
    @Singleton
    fun bindFlashCardsRepository(impl: FlashCardsRepositoryImpl): FlashCardsRepository

    @Binds
    fun bindFlashCardLocalDataSource(impl: FlashCardLocalDataSourceImpl): FlashCardLocalDataSource
}