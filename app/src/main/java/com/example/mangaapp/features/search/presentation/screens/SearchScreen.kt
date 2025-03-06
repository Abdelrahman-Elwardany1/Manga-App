package com.example.mangaapp.features.search.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mangaapp.features.search.domain.models.Manga
import com.example.mangaapp.features.search.presentation.viewmodels.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onMangaClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            label = { Text("Search manga") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val genres = listOf("Action", "Adventure", "Comedy", "Drama", "Fantasy")
            genres.forEach { genre ->
                TextButton(onClick = { viewModel.onGenreSelected(genre) }) {
                    Text(text = genre)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.onSearch() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Search")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        uiState.error?.let { error ->
            Text(text = error, color = MaterialTheme.colors.error)
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(uiState.mangaList) { manga ->
                MangaCard(
                    manga = manga,
                    onClick = { onMangaClick(manga.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MangaCard(
    manga: Manga,
    onClick: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(manga.coverUrl),
                contentDescription = manga.title,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = manga.title,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = manga.description,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}