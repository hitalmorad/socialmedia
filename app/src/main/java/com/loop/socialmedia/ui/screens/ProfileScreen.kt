package com.loop.socialmedia.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loop.socialmedia.ui.theme.md_theme_light_primary
import com.loop.socialmedia.ui.theme.md_theme_light_secondary

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Posts", "Memories", "Boards", "Activity")

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Profile Header
        item {
            ProfileHeader()
        }

        // Stats Row
        item {
            ProfileStats()
        }

        // Action Buttons
        item {
            ProfileActions()
        }

        // Tab Toggle
        item {
            ProfileTabToggle(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs
            )
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                items(getSamplePosts()) { post ->
                    ProfilePostCard(post = post)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            1 -> {
                item {
                    MemoriesTab()
                }
            }
            2 -> {
                item {
                    BoardsTab()
                }
            }
            3 -> {
                item {
                    ActivityTab()
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            md_theme_light_primary.copy(alpha = 0.2f),
                            md_theme_light_secondary.copy(alpha = 0.2f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "https://picsum.photos/200/200?random=1",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(116.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        Text(
            text = "Alex Johnson",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "@alexjohnson",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Photography enthusiast | Travel lover | Coffee addict ☕",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Friendship Score
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Friendship Score",
                tint = md_theme_light_primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Friendship Score: 8.5/10",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ProfileStats() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            count = "1.2K",
            label = "Posts",
            icon = Icons.Default.Image
        )

        StatItem(
            count = "5.8K",
            label = "Followers",
            icon = Icons.Default.People
        )

        StatItem(
            count = "892",
            label = "Following",
            icon = Icons.Default.PersonAdd
        )

        StatItem(
            count = "15",
            label = "Streak",
            icon = Icons.Default.LocalFireDepartment
        )
    }
}

@Composable
private fun StatItem(
    count: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = md_theme_light_primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { /* Handle edit profile */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = md_theme_light_primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile")
        }

        Button(
            onClick = { /* Handle add friend */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = md_theme_light_primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "Add Friend",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Friend")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { /* Handle message */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = md_theme_light_secondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Message",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Message")
        }

        OutlinedButton(
            onClick = { /* Handle settings */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Settings")
        }
    }
}

@Composable
private fun ProfileTabToggle(
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
private fun ProfilePostCard(post: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        AsyncImage(
            model = "https://picsum.photos/400/400?random=${post.hashCode()}",
            contentDescription = "Post",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun MemoriesTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Memories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getMemories()) { memory ->
                MemoryCard(memory = memory)
            }
        }
    }
}

@Composable
private fun MemoryCard(memory: String) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(150.dp)
            .clickable { /* Handle memory click */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = memory,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BoardsTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Inspiration Boards",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getBoards()) { board ->
                BoardCard(board = board)
            }
        }
    }
}

@Composable
private fun BoardCard(board: String) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .clickable { /* Handle board click */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = md_theme_light_secondary.copy(alpha = 0.1f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = board,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ActivityTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Recent Activity",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getActivities()) { activity ->
                ActivityItem(activity = activity)
            }
        }
    }
}

@Composable
private fun ActivityItem(activity: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = "Activity",
            tint = md_theme_light_primary,
            modifier = Modifier.size(8.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = activity,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// Sample data functions
private fun getSamplePosts(): List<String> {
    return listOf("post1", "post2", "post3", "post4", "post5", "post6")
}

private fun getMemories(): List<String> {
    return listOf("1 year ago", "2 years ago", "3 years ago", "5 years ago")
}

private fun getBoards(): List<String> {
    return listOf("Travel Ideas", "Photo Inspiration", "Food Recipes", "Fitness Goals")
}

private fun getActivities(): List<String> {
    return listOf(
        "Liked a post from @mindcast",
        "Commented on @vibeteller's story",
        "Joined Photography Club",
        "Completed daily challenge",
        "Sent 3 compliments today"
    )
}
