package com.example.mangaapp.features.home.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mangaapp.features.home.presentation.viewmodels.HomeViewModel
import com.example.mangaapp.features.search.domain.models.Manga

data class Manga(
    val id: String,
    val title: String,
    val description: String,
    val coverUrl: String
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMangaClick: (String) -> Unit
) {
    val popularManga by viewModel.popularManga.collectAsState()
    val recentManga by viewModel.recentManga.collectAsState()
    val relevantManga by viewModel.relevantManga.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val navigationBarHeight = 80.dp // Consistent height

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
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(
                top = 25.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = navigationBarHeight
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GreetingHeader(
                    profilePicUrl = "https://i.pinimg.com/736x/1a/ec/04/1aec042a9746a4974b83f9b11f987806.jpg",
                    greeting = "Good Morning",
                    userName = "TATY"
                )
            }
            item {
                SectionHeader(title = "Most Popular")
                LazyRow(modifier = Modifier.padding(start = 16.dp)) {
                    items(popularManga) { manga ->
                        MangaCardLarge(manga = manga, onClick = { onMangaClick(manga.id) })
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
                SectionHeader(title = "You May Like")
                LazyRow(modifier = Modifier.padding(start = 16.dp)) {
                    items(relevantManga) { manga ->
                        MangaCard(manga = manga, onClick = { onMangaClick(manga.id) })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun GreetingHeader(
    profilePicUrl: String,
    greeting: String,
    userName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(profilePicUrl),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFDA0037)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
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
            .padding(end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(manga.coverUrl),
                contentDescription = manga.title,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = manga.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(140.dp)
        )
    }
}

@Composable
fun MangaCardLarge(
    manga: Manga,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(380.dp)
            .height(260.dp)
            .padding(end = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(manga.coverUrl),
                contentDescription = manga.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0xAA000000))
            ) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp),
                    maxLines = 2
                )
            }
        }
    }
}