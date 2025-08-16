package com.loop.socialmedia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImage: String = "",
    val content: String = "",
    val mediaUrls: List<String> = emptyList(),
    val mediaType: MediaType = MediaType.IMAGE,
    val likes: List<String> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val shares: Int = 0,
    val views: Int = 0,
    val hashtags: List<String> = emptyList(),
    val location: String = "",
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Comment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImage: String = "",
    val content: String = "",
    val likes: List<String> = emptyList(),
    val replies: List<Comment> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

enum class MediaType {
    IMAGE, VIDEO, TEXT, POLL, STORY, REEL
}
