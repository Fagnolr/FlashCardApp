package com.example.presentation.screens.flashcardrevision

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.screens.home.FlashCardItem

@Composable
fun FlashCardRevisionScreen(
    viewModel: FlashCardRevisionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is FlashCardRevisionUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is FlashCardRevisionUiState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucune flashcard disponible", style = MaterialTheme.typography.bodyLarge)
            }
        }

        is FlashCardRevisionUiState.Success -> {
            FlashCardRevisionContent(
                flashCard = (state as FlashCardRevisionUiState.Success).flashCard,
                showAnswer = (state as FlashCardRevisionUiState.Success).showAnswer,
                onRevealAnswer = { viewModel.revealAnswer() },
                onNextCard = { viewModel.showNextFlashCard() }
            )
        }

        is FlashCardRevisionUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erreur : ${(state as FlashCardRevisionUiState.Error).message}", color = Color.Red)
            }
        }
    }
}

@Composable
fun FlashCardRevisionContent(
    flashCard: FlashCardItem,
    showAnswer: Boolean,
    onRevealAnswer: () -> Unit,
    onNextCard: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Question : ${flashCard.question}",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (showAnswer) {
            Text(
                text = "Réponse : ${flashCard.answer}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else {
            Button(onClick = onRevealAnswer) {
                Text("Afficher la réponse")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onNextCard) {
            Text("Carte suivante")
        }
    }
}
