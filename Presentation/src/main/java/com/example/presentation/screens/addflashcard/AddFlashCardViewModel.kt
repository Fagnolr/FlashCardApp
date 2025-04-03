package com.example.presentation.screens.addflashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.FlashCardModel
import com.example.domain.usecases.AddFlashCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddFlashCardViewModel @Inject constructor(
    private val addFlashCardUseCase: AddFlashCardUseCase
) : ViewModel() {

    private val _addFlashCardFlow = MutableStateFlow<AddFlashCardUiState>(AddFlashCardUiState.Idle)
    val addFlashCardFlow: StateFlow<AddFlashCardUiState> = _addFlashCardFlow

    private val _question = MutableStateFlow("")
    val question: StateFlow<String> = _question

    private val _answer = MutableStateFlow("")
    val answer: StateFlow<String> = _answer

    fun onQuestionChanged(newQuestion: String) {
        _question.value = newQuestion
    }

    fun onAnswerChanged(newAnswer: String) {
        _answer.value = newAnswer
    }

    fun addFlashCard() {
        if (question.value.isBlank() || answer.value.isBlank()) {
            _addFlashCardFlow.value =
                AddFlashCardUiState.Error("Les champs ne peuvent pas être vides")
            return
        }

        viewModelScope.launch {
            _addFlashCardFlow.value = AddFlashCardUiState.Loading
            val result = addFlashCardUseCase(
                FlashCardModel(
                    UUID.randomUUID().toString(),
                    question.value,
                    answer.value
                )
            )

            _addFlashCardFlow.value = result.fold(
                onSuccess = {
                    _question.value = ""
                    _answer.value = ""
                    AddFlashCardUiState.Success

                },
                onFailure = {
                    val errorMessage = when (it) {
                        is IOException -> "Problème de connexion Internet"
                        is IllegalArgumentException -> "Données invalides"
                        else -> it.message ?: "Une erreur inconnue est survenue"
                    }
                    AddFlashCardUiState.Error(errorMessage)
                }
            )
        }
    }

    fun resetAddFlashCardState() {
        _addFlashCardFlow.value = AddFlashCardUiState.Idle
    }
}
