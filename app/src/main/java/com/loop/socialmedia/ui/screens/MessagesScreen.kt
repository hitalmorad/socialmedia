package com.loop.socialmedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loop.socialmedia.data.model.Chat
import com.loop.socialmedia.data.model.Message
import com.loop.socialmedia.ui.theme.md_theme_light_primary
import com.loop.socialmedia.ui.theme.md_theme_light_secondary

@Composable
fun MessagesScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Direct Messages", "Group Chats", "Communities")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        MessagesHeader()

        // Tab Toggle
        MessagesTabToggle(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            tabs = tabs
        )

        // Content based on selected tab
        when (selectedTab) {
            0 -> DirectMessagesTab()
            1 -> GroupChatsTab()
            2 -> CommunitiesTab()
        }
    }
}

@Composable
private fun MessagesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = { /* Handle new message */ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Message",
                    tint = md_theme_light_primary
                )
            }

            IconButton(onClick = { /* Handle search */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun MessagesTabToggle(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index

            Text(
                text = tab,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(index) }
                    .background(
                        if (isSelected) md_theme_light_primary else Color.Transparent,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 12.dp),
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun DirectMessagesTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(getSampleDirectMessages()) { chat ->
            DirectMessageItem(
                chat = chat,
                onClick = { /* Handle chat click */ }
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
private fun DirectMessageItem(
    chat: Chat,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image
        AsyncImage(
            model = chat.groupImage.ifEmpty { "https://picsum.photos/200/200?random=${chat.id.hashCode()}" },
            contentDescription = if (chat.isGroup) chat.groupName else "Profile",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Chat Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (chat.isGroup) chat.groupName else "User Name",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "2m ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = chat.lastMessage?.content ?: "No messages yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }

        // Unread Badge
        if (chat.unreadCount > 0) {
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(md_theme_light_secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun GroupChatsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(getSampleGroupChats()) { chat ->
            GroupChatItem(
                chat = chat,
                onClick = { /* Handle group chat click */ }
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
private fun GroupChatItem(
    chat: Chat,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Group Image
        AsyncImage(
            model = chat.groupImage.ifEmpty { "https://picsum.photos/200/200?random=${chat.id.hashCode()}" },
            contentDescription = chat.groupName,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Group Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.groupName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${chat.participants.size} members",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = chat.lastMessage?.content ?: "No messages yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        // Unread Badge
        if (chat.unreadCount > 0) {
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(md_theme_light_secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CommunitiesTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(getSampleCommunities()) { community ->
            CommunityItem(
                community = community,
                onClick = { /* Handle community click */ }
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
private fun CommunityItem(
    community: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Community Icon
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(md_theme_light_primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = "Community",
                tint = md_theme_light_primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Community Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = community,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Active community with daily discussions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "View Community",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Sample data functions
private fun getSampleDirectMessages(): List<Chat> {
    return listOf(
        Chat(
            id = "1",
            participants = listOf("user1", "user2"),
            lastMessage = Message(
                id = "msg1",
                chatId = "1",
                content = "Hey! How are you doing?",
                senderId = "user2"
            ),
            unreadCount = 2
        ),
        Chat(
            id = "2",
            participants = listOf("user1", "user3"),
            lastMessage = Message(
                id = "msg2",
                chatId = "2",
                content = "Thanks for the compliment!",
                senderId = "user3"
            ),
            unreadCount = 0
        ),
        Chat(
            id = "3",
            participants = listOf("user1", "user4"),
            lastMessage = Message(
                id = "msg3",
                chatId = "3",
                content = "See you tomorrow!",
                senderId = "user1"
            ),
            unreadCount = 1
        )
    )
}

private fun getSampleGroupChats(): List<Chat> {
    return listOf(
        Chat(
            id = "group1",
            participants = listOf("user1", "user2", "user3", "user4"),
            isGroup = true,
            groupName = "Photography Club",
            groupImage = "https://picsum.photos/200/200?random=10",
            lastMessage = Message(
                id = "msg4",
                chatId = "group1",
                content = "Great shot everyone!",
                senderId = "user2"
            ),
            unreadCount = 5
        ),
        Chat(
            id = "group2",
            participants = listOf("user1", "user5", "user6"),
            isGroup = true,
            groupName = "Travel Buddies",
            groupImage = "https://picsum.photos/200/200?random=11",
            lastMessage = Message(
                id = "msg5",
                chatId = "group2",
                content = "Next trip planning?",
                senderId = "user5"
            ),
            unreadCount = 0
        )
    )
}

private fun getSampleCommunities(): List<String> {
    return listOf(
        "Photography Lovers",
        "Travel Enthusiasts",
        "Food Bloggers",
        "Fitness Community",
        "Art & Design",
        "Music Lovers"
    )
}
