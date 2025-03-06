package com.example.mangaapp.features.home.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mangaapp.features.home.presentation.viewmodels.HomeViewModel
import com.example.mangaapp.features.search.domain.models.Manga

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMangaClick: (String) -> Unit
) {
    val popularManga by viewModel.popularManga.collectAsState()
    val recentManga by viewModel.recentManga.collectAsState()
    val comingSoonManga by viewModel.comingSoonManga.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = error ?: "Unknown error", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Image(
                    painter = rememberAsyncImagePainter("https://example.com/banner.jpg"),
                    contentDescription = "Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            item {
                SectionHeader(title = "Popular Manga")
                LazyRow(modifier = Modifier.padding(start = 16.dp)) {
                    items(popularManga) { manga ->
                        MangaCard(manga = manga, onClick = { onMangaClick(manga.id) })
                    }
                }
            }
            item {
                SectionHeader(title = "Recent Releases")
                LazyRow(modifier = Modifier.padding(start = 16.dp)) {
                    items(recentManga) { manga ->
                        MangaCard(manga = manga, onClick = { onMangaClick(manga.id) })
                    }
                }
            }
            item {
                SectionHeader(title = "Coming Soon")
                LazyRow(modifier = Modifier.padding(start = 16.dp)) {
                    items(comingSoonManga) { manga ->
                        MangaCard(manga = manga, onClick = { onMangaClick(manga.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun MangaCard(
    manga: Manga,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .padding(end = 16.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(manga.coverUrl),
            contentDescription = manga.title,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = manga.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}