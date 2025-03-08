package com.example.mangaapp.features.profile.data

import com.example.mangaapp.features.profile.domain.ProfileRepository
import com.example.mangaapp.features.profile.domain.models.ProfileData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl : ProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getUserProfile(userId: String): Result<ProfileData> {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            if (doc.exists()) {
                val firstName = doc.getString("firstName") ?: ""
                val lastName = doc.getString("lastName") ?: ""
                val nickName = doc.getString("nickName") ?: ""
                val profilePicUrl = doc.getString("profilePicUrl")
                Result.success(ProfileData(firstName, lastName, nickName, profilePicUrl))
            } else {
                Result.failure(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfilePic(userId: String, imageUrl: String): Result<Unit> {
        return try {
            firestore.collection("users").document(userId)
                .update("profilePicUrl", imageUrl)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}