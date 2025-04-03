package com.example.domain.repository

import com.example.domain.model.FlashCardModel
import kotlinx.coroutines.flow.Flow

interface FlashCardsRepository {
    suspend fun insertFlashCard(flashCardModel: FlashCardModel)
    fun getFlashCards(): Flow<Result<List<FlashCardModel>>>
    suspend fun removeFlashCard(flashCardModel: FlashCardModel)
}