package com.example.domain.usecases

import com.example.domain.model.FlashCardModel
import com.example.domain.repository.FlashCardsRepository
import javax.inject.Inject

class RemoveFlashCardUseCase @Inject constructor(
    private val flashCardsRepository: FlashCardsRepository,
) {
    suspend operator fun invoke(flashCardModel: FlashCardModel) {
        flashCardsRepository.removeFlashCard(flashCardModel)
    }
}