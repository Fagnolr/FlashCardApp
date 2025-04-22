package com.example.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.FlashCardModel
import com.example.domain.usecases.GetFlashCardsUseCase
import com.example.domain.usecases.RemoveFlashCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    getFlashCardsUseCase: GetFlashCardsUseCase,
    private val removeFlashCardUseCase: RemoveFlashCardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val state: StateFlow<HomeScreenUiState> = _state

    init {
        viewModelScope.launch {
            getFlashCardsUseCase()
                .map { result ->
                    result.fold(
                        onSuccess = { flashCards ->
                            HomeScreenUiState.Success(
                                flashCards = flashCards.map { it.toFlashCardItem() }
                                    .toPersistentList()
                            )
                        },
                        onFailure = { error ->
                            HomeScreenUiState.Error(error.localizedMessage.orEmpty())
                        }
                    )
                }
                .flowOn(Dispatchers.IO)
                .collect { _state.value = it }
        }
    }

    fun deleteFlashCard(flashCardItem: FlashCardItem) {
        viewModelScope.launch {
            try {
                removeFlashCardUseCase(flashCardItem.toFlashCardModel())
            } catch (e: Exception) {
                _state.value = HomeScreenUiState.Error(e.localizedMessage.orEmpty())
            }
        }
    }

    private fun FlashCardModel.toFlashCardItem(): FlashCardItem =
        FlashCardItem(
            id = id,
            question = question,
            answer = answer
        )

    private fun FlashCardItem.toFlashCardModel(): FlashCardModel =
        FlashCardModel(
            id = id,
            question = question,
            answer = answer
        )
}