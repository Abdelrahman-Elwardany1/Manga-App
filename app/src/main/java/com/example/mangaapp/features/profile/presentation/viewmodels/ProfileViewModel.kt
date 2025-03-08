package com.example.mangaapp.features.profile.presentation.viewmodels

import android.net.Uri
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

    fun uploadProfilePic(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                try {
                    val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/$userId.jpg")
                    val uploadTask = storageRef.putFile(uri).await()

                    val downloadUrl = storageRef.downloadUrl.await().toString()
                    val updateResult = repository.updateProfilePic(userId, downloadUrl)
                    if (updateResult.isSuccess) {
                        loadUserProfile(userId)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            error = updateResult.exceptionOrNull()?.message ?: "Profile update failed"
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            } else {
                _uiState.value = _uiState.value.copy(error = "User not logged in")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}