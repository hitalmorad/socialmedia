package com.loop.socialmedia.data.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.cloudinary.utils.ObjectUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudinaryRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    init {
        // Initialize Cloudinary MediaManager with application context
        MediaManager.init(context)
    }

    private val cloudinary: Cloudinary by lazy {
        Cloudinary(
            mapOf(
                "cloud_name" to "duhc5fja9", // ⚠️ Move to BuildConfig for security
                "api_key" to "597938773129959",
                "api_secret" to "DotZ3z0sHqt3UxDARBwRB9DXBtg"
            )
        )
    }

    // -------------------- POSTS --------------------
    suspend fun uploadPost(
        context: Context,
        imageUri: Uri,
        userId: String,
        onProgress: (Float) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            require(userId.isNotBlank()) { "User ID cannot be empty" }
            val filePath = getFilePathFromUri(context, imageUri)
                ?: return@withContext Result.failure(Exception("Failed to retrieve file path"))

            val options = ObjectUtils.asMap(
                "public_id", "$userId/${System.currentTimeMillis()}",
                "folder", "posts", // ✅ Store inside "posts" folder
                "resource_type", "image"
            )

            val uploadResult = cloudinary.uploader().upload(filePath, options) { bytesUploaded, totalBytes ->
                onProgress(if (totalBytes > 0) bytesUploaded.toFloat() / totalBytes.toFloat() else 0f)
            }

            val imageUrl = uploadResult["secure_url"] as String
            cleanupTempFile(filePath)
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // -------------------- REELS --------------------
    suspend fun uploadReel(
        context: Context,
        videoUri: Uri,
        userId: String,
        onProgress: (Float) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            require(userId.isNotBlank()) { "User ID cannot be empty" }
            val filePath = getFilePathFromUri(context, videoUri)
                ?: return@withContext Result.failure(Exception("Failed to retrieve file path"))

            val options = ObjectUtils.asMap(
                "public_id", "$userId/${System.currentTimeMillis()}",
                "folder", "reels", // ✅ Store inside "reels" folder
                "resource_type", "video",
                "chunk_size", 6000000,
                "eager", arrayOf(
                    mapOf("format" to "mp4", "width" to 640, "height" to 480, "crop" to "fill"),
                    mapOf("format" to "mp4", "width" to 1280, "height" to 720, "crop" to "fill")
                )
            )

            val uploadResult = cloudinary.uploader().upload(filePath, options) { bytesUploaded, totalBytes ->
                onProgress(if (totalBytes > 0) bytesUploaded.toFloat() / totalBytes.toFloat() else 0f)
            }

            val videoUrl = uploadResult["secure_url"] as String
            cleanupTempFile(filePath)
            Result.success(videoUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // -------------------- STORIES --------------------
    suspend fun uploadStory(
        context: Context,
        mediaUri: Uri,
        userId: String,
        isVideo: Boolean = false,
        onProgress: (Float) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            require(userId.isNotBlank()) { "User ID cannot be empty" }
            val filePath = getFilePathFromUri(context, mediaUri)
                ?: return@withContext Result.failure(Exception("Failed to retrieve file path"))

            val resourceType = if (isVideo) "video" else "image"
            val options = ObjectUtils.asMap(
                "public_id", "$userId/${System.currentTimeMillis()}",
                "folder", "stories", // ✅ Store inside "stories" folder
                "resource_type", resourceType
            )

            val uploadResult = cloudinary.uploader().upload(filePath, options) { bytesUploaded, totalBytes ->
                onProgress(if (totalBytes > 0) bytesUploaded.toFloat() / totalBytes.toFloat() else 0f)
            }

            val mediaUrl = uploadResult["secure_url"] as String
            cleanupTempFile(filePath)
            Result.success(mediaUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // -------------------- PROFILE IMAGE --------------------
    suspend fun uploadProfileImage(
        context: Context,
        imageUri: Uri,
        userId: String,
        onProgress: (Float) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            require(userId.isNotBlank()) { "User ID cannot be empty" }
            val filePath = getFilePathFromUri(context, imageUri)
                ?: return@withContext Result.failure(Exception("Failed to retrieve file path"))

            val options = ObjectUtils.asMap(
                "public_id", "$userId/${System.currentTimeMillis()}",
                "folder", "profile_image", // ✅ Store inside "profile_image" folder
                "resource_type", "image",
                "transformation", "w_200,h_200,c_fill,g_face"
            )

            val uploadResult = cloudinary.uploader().upload(filePath, options) { bytesUploaded, totalBytes ->
                onProgress(if (totalBytes > 0) bytesUploaded.toFloat() / totalBytes.toFloat() else 0f)
            }

            val imageUrl = uploadResult["secure_url"] as String
            cleanupTempFile(filePath)
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // -------------------- Helpers --------------------
    private fun getFilePathFromUri(context: Context, uri: Uri): String? {
        return try {
            val projection = arrayOf(android.provider.MediaStore.MediaColumns.DATA)
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val columnIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DATA)
                cursor.moveToFirst()
                cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            // Fallback for content:// URIs
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("temp_media", ".tmp", context.cacheDir)
            inputStream?.use { input ->
                tempFile.outputStream().use { output -> input.copyTo(output) }
            }
            tempFile.absolutePath
        }
    }

    private fun cleanupTempFile(filePath: String) {
        val file = File(filePath)
        if (file.exists() && filePath.contains(context.cacheDir.absolutePath)) {
            file.delete()
        }
    }
}
