package com.example.presentation.screens.flashcardrevision

import com.example.presentation.screens.home.FlashCardItem

sealed class FlashCardRevisionUiState {
    object Loading : FlashCardRevisionUiState()
    object Empty : FlashCardRevisionUiState()
    data class Success(
        val flashCard: FlashCardItem,
        val showAnswer: Boolean
    ) : FlashCardRevisionUiState()
    data class Error(val message: String) : FlashCardRevisionUiState()
}
