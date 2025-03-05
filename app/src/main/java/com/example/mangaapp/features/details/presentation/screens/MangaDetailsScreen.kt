package com.example.mangaapp.features.details.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mangaapp.features.details.presentation.viewmodels.MangaDetailsViewModel

@Composable
fun MangaDetailsScreen(
    viewModel: MangaDetailsViewModel,
    mangaId: String,
    onReadClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(mangaId) {
        viewModel.loadMangaDetails(mangaId)
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${uiState.error}")
            }
        }
        else -> {
            uiState.manga?.let { manga ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(manga.coverUrl),
                        contentDescription = manga.title,
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = manga.title,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = manga.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onReadClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Read")
                    }
                }
            }
        }
    }
}