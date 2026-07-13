package ru.example.gitsource.presentation.popular_repositories

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.R

@Composable
fun PopularRepositoriesScreen() {
    Text(
        text = stringResource(R.string.popular_repo_screen),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)

    )
}

@Composable
@Preview
fun PreviewPopularRepoScreen(){
    PopularRepositoriesScreen()
}