package com.example.mangaapp.features.chapters.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mangaapp.features.chapters.domain.models.Chapter
import com.example.mangaapp.features.chapters.presentation.viewmodels.ChapterViewModel

@Composable
fun ChapterScreen(
    viewModel: ChapterViewModel,
    mangaId: String,
    onChapterRead: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(mangaId) {
        viewModel.loadChapters(mangaId)
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.chapters) { chapter ->
                    ChapterItem(chapter = chapter, onReadClick = { chapterId ->
                        onChapterRead(chapterId)
                    })
                }
            }
        }
    }
}

@Composable
fun ChapterItem(
    chapter: Chapter,
    onReadClick: (String) -> Unit
) {
    androidx.compose.material.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Chapter ${chapter.chapterNumber}: ${chapter.title}")
            Text(text = "Pages: ${chapter.pages}")
            Text(text = "Language: ${chapter.language}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Read",
                modifier = Modifier.clickable { onReadClick(chapter.id) },
                color = MaterialTheme.colors.primary
            )
        }
    }
}