package com.example.mangaapp.features.profile.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mangaapp.features.profile.presentation.viewmodels.ProfileViewModel
import com.example.mangaapp.features.search.domain.models.Manga
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.loadUserProfile(userId)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadProfilePic(it, context) }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDA0037))
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    uiState.profileData?.let { profile ->
                        ProfileHeader(
                            profilePicUrl = profile.profilePicUrl,
                            nickName = profile.nickName,
                            textColor = Color.White,
                            onProfilePicClick = { imagePickerLauncher.launch("image/*") }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!uiState.isLoading) {
                    uiState.profileData?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Favorite Manga",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Start),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FavoriteMangaList()
                    }
                    uiState.error?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    profilePicUrl: String?,
    nickName: String,
    textColor: Color = Color.Black,
    onProfilePicClick: () -> Unit
) {
    val painter = if (!profilePicUrl.isNullOrEmpty()) {
        rememberAsyncImagePainter(profilePicUrl)
    } else {
        rememberAsyncImagePainter("https://via.placeholder.com/150")
    }
    Image(
        painter = painter,
        contentDescription = "Profile Picture",
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .clickable { onProfilePicClick() },
        contentScale = ContentScale.Crop
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = nickName,
        style = MaterialTheme.typography.titleLarge,
        color = textColor
    )
}

@Composable
fun FavoriteMangaList() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Favorite Manga 1", color = MaterialTheme.colorScheme.onBackground)
        Text(text = "Favorite Manga 2", color = MaterialTheme.colorScheme.onBackground)
        Text(text = "Favorite Manga 3", color = MaterialTheme.colorScheme.onBackground)
    }
}