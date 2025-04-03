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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
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
            label = { Text(stringResource(R.string.question)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = viewModel::onAnswerChanged,
            label = { Text(stringResource(R.string.answer)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = viewModel::addFlashCard,
            modifier = Modifier.fillMaxWidth(),
            enabled = question.isNotBlank() && answer.isNotBlank()
        ) {
            Text(stringResource(R.string.add_flash_card_button))
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = addState is AddFlashCardUiState.Success || addState is AddFlashCardUiState.Error,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            FlashCardStateMessage(addState)
        }

        if (addState is AddFlashCardUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun FlashCardStateMessage(state: AddFlashCardUiState) {
    when (state) {
        is AddFlashCardUiState.Success -> {
            Text(
                text = stringResource(R.string.add_flash_card_success),
                color = Color.Green,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        is AddFlashCardUiState.Error -> {
            Text(
                text = state.message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        else -> Unit
    }
}