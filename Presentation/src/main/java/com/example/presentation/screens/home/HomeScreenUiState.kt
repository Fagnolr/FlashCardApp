package com.example.presentation.screens.home

import kotlinx.collections.immutable.PersistentList

interface HomeScreenUiState {
    object Loading : HomeScreenUiState
    data class Success(
        val flashCards: PersistentList<FlashCardItem>
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}

data class FlashCardItem(
    val id: String,
    val question: String,
    val answer: String,
)
