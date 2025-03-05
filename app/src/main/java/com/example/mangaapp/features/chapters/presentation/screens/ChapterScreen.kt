package com.example.mangaapp.features.chapters.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mangaapp.features.chapters.presentation.viewmodels.ChapterViewModel

@Composable
fun ChapterScreen(
    viewModel: ChapterViewModel,
    mangaId: String
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(mangaId) {
        viewModel.loadChapters(mangaId)
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = "Error: ${uiState.error}")
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.chapters) { chapter ->
                    ChapterItem(chapter = chapter)
                }
            }
        }
    }
}

@Composable
fun ChapterItem(chapter: com.example.mangaapp.features.chapters.domain.models.Chapter) {
    androidx.compose.material.Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Chapter ${chapter.chapterNumber}: ${chapter.title}")
            Text(text = "Pages: ${chapter.pages}")
            Text(text = "Language: ${chapter.language}")
        }
    }
}