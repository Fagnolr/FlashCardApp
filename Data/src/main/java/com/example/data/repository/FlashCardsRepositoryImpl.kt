package com.example.data.repository

import com.example.data.data_source.local.FlashCardLocalDataSource
import com.example.domain.model.FlashCardModel
import com.example.domain.repository.FlashCardsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlashCardsRepositoryImpl @Inject constructor(
    private val flashCardLocalDataSource: FlashCardLocalDataSource
) : FlashCardsRepository {
    override suspend fun insertFlashCard(flashCardModel: FlashCardModel) {
        flashCardLocalDataSource.insertFlashCard(flashCardModel)
    }

    override fun getFlashCards(): Flow<Result<List<FlashCardModel>>> =
        flashCardLocalDataSource.getFlashCards()

    override suspend fun removeFlashCard(flashCardModel: FlashCardModel) {
        flashCardLocalDataSource.removeFlashCard(flashCardModel)
    }
}
