package com.example.mangaapp.features.authentication.data

import com.example.mangaapp.features.authentication.domain.UserProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImpl : UserProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun createUserProfile(
        userId: String,
        firstName: String,
        lastName: String,
        nickName: String
    ): Result<Unit> {
        return try {
            val data = mapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "nickName" to nickName
            )
            firestore.collection("users").document(userId).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}