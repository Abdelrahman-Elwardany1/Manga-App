package com.example.mangaapp.features.chapterreader.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mangaapp.features.chapterreader.presentation.viewmodels.ChapterReaderViewModel

@Composable
fun ChapterReaderScreen(
    chapterId: String,
    viewModel: ChapterReaderViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(chapterId) {
        viewModel.loadChapterPages(chapterId)
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${uiState.error}", style = MaterialTheme.typography.h6)
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.pages) { page ->
                    Image(
                        painter = rememberAsyncImagePainter(page.url),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}