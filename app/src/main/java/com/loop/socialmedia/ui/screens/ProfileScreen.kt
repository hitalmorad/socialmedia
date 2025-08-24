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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.model.User
import com.loop.socialmedia.ui.theme.md_theme_light_primary
import com.loop.socialmedia.ui.theme.md_theme_light_secondary
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Posts", "Memories", "Boards", "Activity")
    var user by remember { mutableStateOf<User?>(null) }
    var posts by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var stories by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var reels by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var activities by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Fetch user data and related content
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                errorMessage = "User not authenticated"
                isLoading = false
                return@launch
            }

            // Fetch user profile
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    user = document.toObject(User::class.java)
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    errorMessage = "Failed to load profile: ${e.message}"
                    isLoading = false
                }

            // Fetch posts
            firestore.collection("posts").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { snapshot ->
                    posts = snapshot.documents.mapNotNull { it.data }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Failed to load posts: ${e.message}"
                }

            // Fetch stories (memories)
            firestore.collection("stories").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { snapshot ->
                    stories = snapshot.documents.mapNotNull { it.data }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Failed to load stories: ${e.message}"
                }

            // Fetch reels
            firestore.collection("reels").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { snapshot ->
                    reels = snapshot.documents.mapNotNull { it.data }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Failed to load reels: ${e.message}"
                }

            // Fetch activities (simulated as a list of strings for simplicity)
            firestore.collection("activities").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { snapshot ->
                    activities = snapshot.documents.mapNotNull { it.getString("description") }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Failed to load activities: ${e.message}"
                }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Profile Header
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                ProfileHeader(user = user)
            }
        }

        // Stats Row
        item {
            ProfileStats(user = user)
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
                if (posts.isEmpty() && !isLoading) {
                    item {
                        Text(
                            text = "No posts available",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    items(posts) { post ->
                        ProfilePostCard(post = post)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            1 -> {
                item {
                    MemoriesTab(stories = stories)
                }
            }
            2 -> {
                item {
                    BoardsTab(interests = user?.interests ?: emptyList())
                }
            }
            3 -> {
                item {
                    ActivityTab(activities = activities)
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(user: User?) {
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
            if (user?.profileImageUrl?.isNotEmpty() == true) {
                AsyncImage(
                    model = user.profileImageUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(116.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "No Image",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        Text(
            text = user?.name ?: "No Name",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = user?.username?.let { "@$it" } ?: "@unknown",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = user?.bio?.takeIf { it.isNotEmpty() } ?: "No bio available",
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
                text = "Friendship Score: ${user?.friendshipScore?.let { "${it}/10" } ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ProfileStats(user: User?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            count = user?.posts?.size?.toString() ?: "0",
            label = "Posts",
            icon = Icons.Default.Image
        )

        StatItem(
            count = user?.followers?.size?.toString() ?: "0",
            label = "Followers",
            icon = Icons.Default.People
        )

        StatItem(
            count = user?.following?.size?.toString() ?: "0",
            label = "Following",
            icon = Icons.Default.PersonAdd
        )

        StatItem(
            count = user?.streakCount?.toString() ?: "0",
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
private fun ProfilePostCard(post: Map<String, Any>) {
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
            model = post["imageUrl"] as? String ?: "https://picsum.photos/400/400",
            contentDescription = "Post",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun MemoriesTab(stories: List<Map<String, Any>>) {
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

        if (stories.isEmpty()) {
            Text(
                text = "No memories available",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stories) { story ->
                    MemoryCard(story = story)
                }
            }
        }
    }
}

@Composable
private fun MemoryCard(story: Map<String, Any>) {
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
            AsyncImage(
                model = story["imageUrl"] as? String ?: "https://picsum.photos/200/150",
                contentDescription = "Memory",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = story["content"] as? String ?: "No description",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun BoardsTab(interests: List<String>) {
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

        if (interests.isEmpty()) {
            Text(
                text = "No interests selected",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(interests) { interest ->
                    BoardCard(interest = interest)
                }
            }
        }
    }
}

@Composable
private fun BoardCard(interest: String) {
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
                text = interest,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ActivityTab(activities: List<String>) {
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

        if (activities.isEmpty()) {
            Text(
                text = "No recent activity",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(activities) { activity ->
                    ActivityItem(activity = activity)
                }
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