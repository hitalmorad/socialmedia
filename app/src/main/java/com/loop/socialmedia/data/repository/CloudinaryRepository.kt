package com.loop.socialmedia.data.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudinaryRepository @Inject constructor() {

    private val cloudinary: Cloudinary by lazy {
        Cloudinary(
            mapOf(
                "cloud_name" to "duhc5fja9",
                "api_key" to "597938773129959",
                "api_secret" to "DotZ3z0sHqt3UxDARBwRB9DXBtg"
            )
        )
    }

    suspend fun uploadPost(
        context: Context,
        imageUri: Uri,
        userId: String,
        onProgress: (Float) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val filePath = getFilePathFromUri(context, imageUri)
            if (filePath == null) {
                return@withContext Result.failure(Exception("Failed to retrieve file path"))
            }

            val options = ObjectUtils.asMap(
                "public_id", "posts/${userId}/${System.currentTimeMillis()}",
                "resource_type", "image"
            )

            val uploadResult = cloudinary.uploader().upload(filePath, options)
            val imageUrl = uploadResult["secure_url"] as String

            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadReel(
        context: Context,
        videoUri: Uri,
        userId: String,
        onProgress: (Float) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val filePath = getFilePathFromUri(context, videoUri)
            if (filePath == null) {
                return@withContext Result.failure(Exception("Failed to retrieve file path"))
            }

            val options = ObjectUtils.asMap(
                "public_id", "reels/${userId}/${System.currentTimeMillis()}",
                "resource_type", "video",
                "chunk_size", 6000000,
                "eager", "mp4_640x480,mp4_1280x720"
            )

            val uploadResult = cloudinary.uploader().upload(filePath, options)
            val videoUrl = uploadResult["secure_url"] as String

            Result.success(videoUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadStory(
        context: Context,
        mediaUri: Uri,
        userId: String,
        isVideo: Boolean = false,
        onProgress: (Float) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val filePath = getFilePathFromUri(context, mediaUri)
            if (filePath == null) {
                return@withContext Result.failure(Exception("Failed to retrieve file path"))
            }

            val resourceType = if (isVideo) "video" else "image"
            val options = ObjectUtils.asMap(
                "public_id", "stories/${userId}/${System.currentTimeMillis()}",
                "resource_type", resourceType
            )

            val uploadResult = cloudinary.uploader().upload(filePath, options)
            val mediaUrl = uploadResult["secure_url"] as String

            Result.success(mediaUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
}
