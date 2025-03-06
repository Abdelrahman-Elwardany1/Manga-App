package com.example.mangaapp.features.details.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mangaapp.features.chapters.domain.models.Chapter
import com.example.mangaapp.features.details.domain.models.MangaDetails
import com.example.mangaapp.features.details.presentation.viewmodels.MangaDetailsViewModel

@Composable
fun MangaDetailsScreen(
    manga: MangaDetails,
    chapters: List<Chapter>,
    onChapterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Box(modifier = Modifier.height(300.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(manga.coverUrl),
                    contentDescription = manga.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                                startY = 150f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = manga.title,
                        style = androidx.compose.material.MaterialTheme.typography.h5,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Synopsis",
                    style = androidx.compose.material.MaterialTheme.typography.h6,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = manga.description,
                    style = androidx.compose.material.MaterialTheme.typography.body2,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            Text(
                text = "Chapters",
                style = androidx.compose.material.MaterialTheme.typography.h6,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(chapters) { chapter ->
            ChapterListItem(
                chapter = chapter,
                onChapterClick = onChapterClick
            )
        }
    }
}

@Composable
fun ChapterListItem(
    chapter: Chapter,
    onChapterClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onChapterClick(chapter.id) },
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Chapter ${chapter.chapterNumber}",
                    style = androidx.compose.material.MaterialTheme.typography.body1,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = chapter.title,
                    style = androidx.compose.material.MaterialTheme.typography.body2,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}