package com.loop.socialmedia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderProfileImage: String = "",
    val content: String = "",
    val mediaUrl: String = "",
    val mediaType: MessageMediaType = MessageMediaType.TEXT,
    val isRead: Boolean = false,
    val isDeleted: Boolean = false,
    val replyTo: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Chat(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: Message? = null,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val groupName: String = "",
    val groupImage: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class MessageMediaType {
    TEXT, IMAGE, VIDEO, AUDIO, LOCATION, POLL
}
