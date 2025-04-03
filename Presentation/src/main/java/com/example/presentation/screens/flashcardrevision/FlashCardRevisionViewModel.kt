package com.example.presentation.screens.flashcardrevision

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.FlashCardModel
import com.example.domain.usecases.GetFlashCardsUseCase
import com.example.presentation.screens.home.FlashCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashCardRevisionViewModel @Inject constructor(
    private val getFlashCardsUseCase: GetFlashCardsUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow<FlashCardRevisionUiState>(FlashCardRevisionUiState.Loading)
    val state: StateFlow<FlashCardRevisionUiState> = _state

    private var flashCards: List<FlashCardItem> = emptyList()
    private var currentIndex: Int? = null

    init {
        loadFlashCards()
    }

    private fun loadFlashCards() {
        viewModelScope.launch {
            getFlashCardsUseCase().collect { result ->
                result.onSuccess { cards ->
                    flashCards = cards.map { it.toFlashCardItem() }.shuffled()
                    showNextFlashCard()
                }.onFailure {
                    _state.value = FlashCardRevisionUiState.Error(it.localizedMessage.orEmpty())
                }
            }
        }
    }

    fun showNextFlashCard() {
        if (flashCards.isNotEmpty()) {
            currentIndex = (flashCards.indices - currentIndex).randomOrNull()
            _state.value =
                FlashCardRevisionUiState.Success(flashCards[currentIndex ?: 0], showAnswer = false)
        } else {
            _state.value = FlashCardRevisionUiState.Empty
        }
    }

    fun revealAnswer() {
        val currentState = _state.value
        if (currentState is FlashCardRevisionUiState.Success) {
            _state.value = currentState.copy(showAnswer = true)
        }
    }

    private fun FlashCardModel.toFlashCardItem(): FlashCardItem {
        return FlashCardItem(
            id = this.id,
            question = this.question,
            answer = this.answer
        )
    }
}
