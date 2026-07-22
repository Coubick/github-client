package ru.example.gitsource.presentation.popular

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.R
import ru.example.gitsource.theme.GitSourceTheme

@Composable
fun PopularRepositoriesScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.popular_repo_screen),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewPopularRepoScreen(){
    GitSourceTheme(darkTheme = true) {
        androidx.compose.material3.Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            PopularRepositoriesScreen()
        }
    }
}
