package com.loop.socialmedia.data.repository

import androidx.room.util.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(
        email: String,
        password: String,
        user: User
    ): Result<User> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Failed to create user")

            val newUser = user.copy(id = userId, email = email)
            firestore.collection("users").document(userId).set(newUser).await()

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Failed to sign in")

            val userDoc = firestore.collection("users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java) ?: throw Exception("User not found")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun getCurrentUser(): User? = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid ?: return@withContext null
            val userDoc = firestore.collection("users").document(userId).get().await()
            userDoc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUser(user: User): Result<User> = withContext(Dispatchers.IO) {
        try {
            firestore.collection("users").document(user.id).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java) ?: throw Exception("User not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchUsers(query: String): Result<List<User>> = withContext(Dispatchers.IO) {
        try {
            val usersQuery = firestore.collection("users")
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + '\uf8ff')
                .limit(20)
                .get()
                .await()

            val users = usersQuery.documents.mapNotNull { it.toObject(User::class.java) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun followUser(userId: String, targetUserId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val batch = firestore.batch()

            // Add to current user's following
            val currentUserRef = firestore.collection("users").document(userId)
            batch.update(currentUserRef, "following", com.google.firebase.firestore.FieldValue.arrayUnion(targetUserId))

            // Add to target user's followers
            val targetUserRef = firestore.collection("users").document(targetUserId)
            batch.update(targetUserRef, "followers", com.google.firebase.firestore.FieldValue.arrayUnion(userId))

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unfollowUser(userId: String, targetUserId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val batch = firestore.batch()

            // Remove from current user's following
            val currentUserRef = firestore.collection("users").document(userId)
            batch.update(currentUserRef, "following", com.google.firebase.firestore.FieldValue.arrayRemove(targetUserId))

            // Remove from target user's followers
            val targetUserRef = firestore.collection("users").document(targetUserId)
            batch.update(targetUserRef, "followers", com.google.firebase.firestore.FieldValue.arrayRemove(userId))

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
