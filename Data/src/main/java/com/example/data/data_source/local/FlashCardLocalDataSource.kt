package com.example.data.data_source.local

import com.example.domain.model.FlashCardModel
import kotlinx.coroutines.flow.Flow

interface FlashCardLocalDataSource {
    suspend fun insertFlashCard(flashCard: FlashCardModel)
    fun getFlashCards(): Flow<Result<List<FlashCardModel>>>
    suspend fun removeFlashCard(flashCard: FlashCardModel)
}