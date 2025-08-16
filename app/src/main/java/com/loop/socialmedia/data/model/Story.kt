package com.loop.socialmedia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImage: String = "",
    val mediaUrl: String = "",
    val mediaType: StoryMediaType = StoryMediaType.IMAGE,
    val caption: String = "",
    val hashtags: List<String> = emptyList(),
    val location: String = "",
    val views: List<String> = emptyList(),
    val replies: List<StoryReply> = emptyList(),
    val isViewed: Boolean = false,
    val isReplied: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000) // 24 hours
) : Parcelable

@Parcelize
data class StoryReply(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImage: String = "",
    val content: String = "",
    val mediaUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

enum class StoryMediaType {
    IMAGE, VIDEO, TEXT, POLL, MUSIC
}
