package cu.jaco.compassconcurrent.feature.main.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cu.jaco.compassconcurrent.R
import cu.jaco.compassconcurrent.feature.main.MainViewModel
import cu.jaco.compassconcurrent.feature.main.ui.states.ScreenState
import cu.jaco.compassconcurrent.ui.theme.CompassConcurrentTheme
import cu.jaco.compassconcurrent.ui.theme.Space16
import cu.jaco.compassconcurrent.ui.theme.Space8
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
) {
    val screenState by mainViewModel.screenState.collectAsStateWithLifecycle()

    CompassConcurrentTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            MainScreen(
                screenState = screenState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                onRefreshClick = mainViewModel::onRefreshClick,
            )
        }
    }
}

@Composable
private fun MainScreen(
    screenState: ScreenState,
    modifier: Modifier = Modifier,
    onRefreshClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ScreenContent(
            screenState = screenState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
        Button(
            enabled = !screenState.isLoading,
            onClick = onRefreshClick,
            modifier = Modifier.padding(Space16)
        ) {
            Text(stringResource(id = R.string.refresh))
        }
    }
}

@Composable
private fun ScreenContent(
    screenState: ScreenState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (screenState.error != null) {
            Error(
                errorMessage = stringResource(id = screenState.error),
                modifier = Modifier
                    .padding(Space16)
                    .fillMaxSize(),
            )
        } else {
            Content(
                wordCount = screenState.wordCount,
                every10thCharacter = screenState.every10thCharacter,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun Content(
    wordCount: Int,
    every10thCharacter: ImmutableList<Char>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 32.dp),
        contentPadding = PaddingValues(Space16),
        verticalArrangement = Arrangement.spacedBy(Space8),
        horizontalArrangement = Arrangement.spacedBy(Space8),
        modifier = modifier
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = wordsCountStringResource(wordCount),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        items(every10thCharacter) { character ->
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            ) {
                Text(
                    text = character.toString(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(Space8),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun Error(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
        )
    }
}

@Composable
@ReadOnlyComposable
fun wordsCountStringResource(count: Int): String = if (count == 0) {
    stringResource(id = R.string.no_words_yet)
} else {
    pluralStringResource(id = R.plurals.word_count, count = count, count)
}
