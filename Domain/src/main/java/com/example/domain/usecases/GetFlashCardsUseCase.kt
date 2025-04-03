package com.example.domain.usecases

import com.example.domain.model.FlashCardModel
import com.example.domain.repository.FlashCardsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFlashCardsUseCase @Inject constructor(
    private val flashCardsRepository: FlashCardsRepository,
) {
    operator fun invoke(): Flow<Result<List<FlashCardModel>>> =
        flashCardsRepository.getFlashCards()
}