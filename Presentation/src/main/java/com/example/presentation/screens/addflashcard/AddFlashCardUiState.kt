package com.example.presentation.screens.addflashcard

interface AddFlashCardUiState {
    object Idle : AddFlashCardUiState
    object Loading : AddFlashCardUiState
    object Success : AddFlashCardUiState
    data class Error(val message: String) : AddFlashCardUiState
}
