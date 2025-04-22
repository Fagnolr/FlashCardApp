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
    private val seenIndices = mutableSetOf<Int>()

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
        if (flashCards.isEmpty()) {
            _state.value = FlashCardRevisionUiState.Empty
            return
        }
        if (seenIndices.size == flashCards.size) {
            seenIndices.clear()
            flashCards = flashCards.shuffled()
        }

        val availableIndices = flashCards.indices.filterNot { it in seenIndices }
        val nextIndex = availableIndices.randomOrNull()
        if (nextIndex != null) {
            seenIndices.add(nextIndex)
            currentIndex = nextIndex
            _state.value =
                FlashCardRevisionUiState.Success(flashCards[nextIndex], showAnswer = false)
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
