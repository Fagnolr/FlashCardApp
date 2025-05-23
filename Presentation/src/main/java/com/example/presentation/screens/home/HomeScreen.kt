package com.example.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import kotlinx.collections.immutable.PersistentList

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        viewModel = viewModel
    )
}

@Composable
fun HomeContent(
    state: HomeScreenUiState,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is HomeScreenUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeScreenUiState.Success -> {
            FlashCardList(state.flashCards, viewModel, modifier)
        }

        is HomeScreenUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.error_with_argument, state.message))
            }
        }
    }
}

@Composable
fun FlashCardList(
    flashCards: PersistentList<FlashCardItem>,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
) {
    if (flashCards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_flashcard_available))
        }
    } else {
        LazyColumn {
            items(
                flashCards,
                key = { it.id }
            ) { flashCard ->
                FlashCard(flashCard, viewModel, modifier)
            }
        }
    }
}

@Composable
fun FlashCard(
    flashCardItem: FlashCardItem,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.3f },
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                viewModel.deleteFlashCard(flashCardItem)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .background(Color.Red, shape = RoundedCornerShape(14.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        }
    ) {
        ElevatedCard(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
        ) {
            Column {
                Text(
                    text = stringResource(R.string.question_with_argument, flashCardItem.question),
                    modifier = Modifier
                        .padding(16.dp),
                )
                HorizontalDivider(thickness = 1.dp)
                Text(
                    text = stringResource(R.string.answer_with_argument, flashCardItem.answer),
                    modifier = Modifier
                        .padding(16.dp),
                )
            }
        }
    }
}