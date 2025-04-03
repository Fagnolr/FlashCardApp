package com.example.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.FlashCardModel
import com.example.domain.usecases.GetFlashCardsUseCase
import com.example.domain.usecases.RemoveFlashCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    getFlashCardsUseCase: GetFlashCardsUseCase,
    private val removeFlashCardUseCase: RemoveFlashCardUseCase
) : ViewModel() {

    val state: StateFlow<HomeScreenUiState> = getFlashCardsUseCase()
        .map { flashCardsResult ->
            flashCardsResult.fold(
                onSuccess = { flashCards ->
                    HomeScreenUiState.Success(
                        flashCards = flashCards.map { it.toFlashCardItem() }.toPersistentList()
                    )
                },
                onFailure = { error ->
                    HomeScreenUiState.Error(error)
                }
            )
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeScreenUiState.Loading
        )

    fun deleteFlashCard(flashCardItem: FlashCardItem) {
        viewModelScope.launch {
            try {
                removeFlashCardUseCase(flashCardItem.toFlashCardModel())
            } catch (e: Exception) {
                Log.d("dede", e.message.orEmpty())
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