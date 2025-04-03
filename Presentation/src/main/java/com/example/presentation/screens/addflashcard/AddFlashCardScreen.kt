package com.example.presentation.screens.addflashcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
fun AddFlashCardScreen(
    viewModel: AddFlashCardViewModel = hiltViewModel()
) {
    val question by viewModel.question.collectAsStateWithLifecycle()
    val answer by viewModel.answer.collectAsStateWithLifecycle()
    val addState by viewModel.addFlashCardFlow.collectAsStateWithLifecycle()

    LaunchedEffect(addState) {
        if (addState is AddFlashCardUiState.Success || addState is AddFlashCardUiState.Error) {
            delay(2000)
            viewModel.resetAddFlashCardState()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = question,
            onValueChange = viewModel::onQuestionChanged,
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = viewModel::onAnswerChanged,
            label = { Text("Réponse") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = viewModel::addFlashCard,
            modifier = Modifier.fillMaxWidth(),
            enabled = question.isNotBlank() && answer.isNotBlank()
        ) {
            Text("Ajouter la carte")
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = addState is AddFlashCardUiState.Success || addState is AddFlashCardUiState.Error,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            when (addState) {
                is AddFlashCardUiState.Error -> {
                    Text(
                        text = (addState as AddFlashCardUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is AddFlashCardUiState.Success -> {
                    Text(
                        text = "Carte ajoutée avec succès !",
                        color = Color.Green,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                else -> Unit
            }
        }

        if (addState is AddFlashCardUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        }
    }
}
