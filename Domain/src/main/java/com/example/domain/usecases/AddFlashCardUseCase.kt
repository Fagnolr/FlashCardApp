package com.example.domain.usecases

import com.example.domain.model.FlashCardModel
import com.example.domain.repository.FlashCardsRepository
import javax.inject.Inject

class AddFlashCardUseCase @Inject constructor(
    private val flashCardsRepository: FlashCardsRepository,
) {
    suspend operator fun invoke(flashCardModel: FlashCardModel): Result<Unit> {
        if (flashCardModel.question.isBlank() || flashCardModel.answer.isBlank()) {
            return Result.failure(IllegalArgumentException("Question et réponse ne peuvent pas être vides"))
        }

        return try {
            flashCardsRepository.insertFlashCard(flashCardModel)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}