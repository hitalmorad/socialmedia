package com.loop.socialmedia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val bio: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val interests: List<String> = emptyList(),
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val posts: List<String> = emptyList(),
    val stories: List<String> = emptyList(),
    val reels: List<String> = emptyList(),
    val friendshipScore: Int = 0,
    val streakCount: Int = 0,
    val isVerified: Boolean = false,
    val isPrivate: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis()
) : Parcelable
