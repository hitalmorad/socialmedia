package com.loop.socialmedia.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loop.socialmedia.R
import com.loop.socialmedia.data.model.MediaType
import com.loop.socialmedia.data.model.Post
import com.loop.socialmedia.data.model.Story
import com.loop.socialmedia.data.model.User
import com.loop.socialmedia.ui.theme.md_theme_light_primary
import com.loop.socialmedia.ui.theme.md_theme_light_secondary

@Composable
fun HomeScreen(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("For You", "Friends")

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header
        item {
            HomeHeader()
        }

        // Stories Row
        item {
            StoriesRow()
        }

        // Feed Toggle
        item {
            FeedToggle(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs
            )
        }

        // Posts
        items(getSamplePosts()) { post ->
            PostCard(
                post = post,
                onLikeClick = { /* Handle like */ },
                onCommentClick = { /* Handle comment */ },
                onShareClick = { /* Handle share */ },
                onMoreClick = { /* Handle more */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Logo
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = md_theme_light_primary
        )

        // Action Icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Heart icon (notifications)
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )

            // Chat icon with notification badge
            Box {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Messages",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )

                // Notification badge
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 12.dp, y = (-4).dp)
                        .background(
                            md_theme_light_secondary,
                            CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun StoriesRow() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Your Loop
        item {
            StoryItem(
                story = Story(
                    id = "your_loop",
                    userName = stringResource(R.string.your_loop),
                    userProfileImage = "https://picsum.photos/200/200?random=1"
                ),
                isYourStory = true,
                onClick = { /* Handle your story */ }
            )
        }

        // Other stories
        items(getSampleStories()) { story ->
            StoryItem(
                story = story,
                isYourStory = false,
                onClick = { /* Handle story click */ }
            )
        }
    }
}

@Composable
private fun StoryItem(
    story: Story,
    isYourStory: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isYourStory) {
                        Brush.radialGradient(
                            colors = listOf(
                                md_theme_light_primary,
                                md_theme_light_secondary
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFE5E7EB),
                                Color(0xFFD1D5DB)
                            )
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isYourStory) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Story",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                AsyncImage(
                    model = story.userProfileImage,
                    contentDescription = story.userName,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = story.userName,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}

@Composable
private fun FeedToggle(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) md_theme_light_primary else Color.Transparent,
                animationSpec = tween(300),
                label = "tab_background"
            )

            Text(
                text = tab,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(index) }
                    .background(
                        backgroundColor,
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
private fun PostCard(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column {
            // User info header with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF5E62),
                                Color(0xFFFF9966)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User avatar with border
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 2.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .padding(2.dp)
                    ) {
                        AsyncImage(
                            model = post.userProfileImage,
                            contentDescription = post.userName,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = post.userName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            if (post.userName == "mindcast") {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = "Imam Majboor, Neha Nasi, Khani Kondu",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }

                    // Follow button
                    Button(
                        onClick = { /* Handle follow */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "Follow",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Post content
            if (post.mediaUrls.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f)
                ) {
                    AsyncImage(
                        model = post.mediaUrls.first(),
                        contentDescription = "Post content",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Play button overlay for video
                    if (post.mediaType == MediaType.VIDEO) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleFilled,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Like button with count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(onClick = onLikeClick)
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) Color(0xFFFF5E62) else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.likes.size}k",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Comment button with count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(onClick = onCommentClick)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.comments.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Save/Bookmark button
                IconButton(onClick = { /* Handle save */ }) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Post caption
            if (post.content.isNotEmpty()) {
                Text(
                    text = post.content,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    lineHeight = 20.sp
                )
            }

            // Hashtags
            if (post.hashtags.isNotEmpty()) {
                Text(
                    text = post.hashtags.joinToString(" ") { "#$it" },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1E90FF),
                    fontWeight = FontWeight.Bold
                )
            }

            // Timestamp and view count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${formatTimeAgo(post.createdAt)} • ${post.views}k views",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Helper function to format timestamp
private fun formatTimeAgo(timestamp: Long): String {
    val seconds = (System.currentTimeMillis() - timestamp) / 1000
    return when {
        seconds < 60 -> "${seconds.toInt()}s"
        seconds < 3600 -> "${(seconds / 60).toInt()}m"
        seconds < 86400 -> "${(seconds / 3600).toInt()}h"
        seconds < 604800 -> "${(seconds / 86400).toInt()}d"
        seconds < 2592000 -> "${(seconds / 604800).toInt()}w"
        seconds < 31536000 -> "${(seconds / 2592000).toInt()}mo"
        else -> "${(seconds / 31536000).toInt()}y"
    }
}

// Sample data functions
private fun getSampleStories(): List<Story> {
    return listOf(
        Story(
            id = "mindcast",
            userName = "mindcast",
            userProfileImage = "https://picsum.photos/200/200?random=2"
        ),
        Story(
            id = "vibeteller",
            userName = "vibeteller",
            userProfileImage = "https://picsum.photos/200/200?random=3"
        ),
        Story(
            id = "moodrealms",
            userName = "moodrealms",
            userProfileImage = "https://picsum.photos/200/200?random=4"
        ),
        Story(
            id = "inkdrop",
            userName = "inkdrop",
            userProfileImage = "https://picsum.photos/200/200?random=5"
        )
    )
}

private fun getSamplePosts(): List<Post> {
    return listOf(
        Post(
            id = "1",
            userId = "mindcast",
            userName = "mindcast",
            userProfileImage = "https://picsum.photos/200/200?random=2",
            content = "@vibeteller, @mooddreamis and others liked this post! @mindcast soft hues, slow days, and a heart full of stillness 🪷",
            mediaUrls = listOf("https://picsum.photos/400/400?random=10"),
            mediaType = com.loop.socialmedia.data.model.MediaType.IMAGE,
            likes = List(107000) { "user$it" },
            isLiked = true
        ),
        Post(
            id = "2",
            userId = "moodreamis",
            userName = "moodreamis",
            userProfileImage = "https://picsum.photos/200/200?random=6",
            content = "Imam Majboor, Neha Nasi, Kinavu Kondu",
            mediaUrls = listOf("https://picsum.photos/400/400?random=11"),
            mediaType = com.loop.socialmedia.data.model.MediaType.IMAGE,
            likes = List(50000) { "user$it" }
        )
    )
}
