package com.example.mangaapp.features.profile.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.features.profile.data.ProfileRepositoryImpl
import com.example.mangaapp.features.profile.domain.ProfileRepository
import com.example.mangaapp.features.profile.presentation.models.ProfileUiState
import com.example.mangaapp.features.search.domain.models.Manga
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.getUserProfile(userId)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    profileData = result.getOrNull(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message,
                    isLoading = false
                )
            }
        }
    }

    fun uploadProfilePic(uri: Uri, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                _uiState.update { it.copy(error = "User not logged in") }
                return@launch
            }
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    _uiState.update { it.copy(error = "Failed to open image input stream") }
                    return@launch
                }
                val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/$userId.jpg")

                storageRef.putStream(inputStream).await()

                val downloadUrl = storageRef.downloadUrl.await().toString()

                val updateResult = repository.updateProfilePic(userId, downloadUrl)
                if (updateResult.isSuccess) {
                    loadUserProfile(userId)
                } else {
                    _uiState.update { it.copy(error = updateResult.exceptionOrNull()?.message ?: "Profile update failed") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error during upload") }
                e.printStackTrace()
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}