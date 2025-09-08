package com.loop.socialmedia.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import kotlin.random.Random
import kotlin.random.Random.Default.nextFloat
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.loop.socialmedia.R
import com.loop.socialmedia.ui.theme.*

// Data classes
data class ContentItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val likes: Int,
    val comments: Int
)

data class Community(
    val id: String,
    val name: String,
    val imageUrl: String,
    val members: Int
)

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isVerified: Boolean = false,
    val isFollowing: Boolean = false
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    onSearch: (String, String) -> Unit = { _, _ -> },
    onContentClick: (String) -> Unit = {},
    onGroupClick: (String) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("For You") }
    val categories = listOf("For You", "Trending", "New", "Following")
    var searchQuery by remember { mutableStateOf("") }
    var showSearchFilters by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Bar with Filters
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { onSearch(searchQuery, selectedCategory) },
                onFilterClick = { showSearchFilters = !showSearchFilters },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Search Filters (initially hidden)
        if (showSearchFilters) {
            SearchFilters(
                onFilterSelected = { category ->
                    selectedCategory = category
                    showSearchFilters = false
                },
                categories = categories,
                selectedCategory = selectedCategory,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Main Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Popular Polls Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Popular Polls",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Sample Poll Item - Replace with actual poll data
                    PollItem(
                        question = "Which social media platform do you use the most?",
                        options = listOf("Instagram", "Twitter", "TikTok", "Other"),
                        onVote = { /* Handle vote */ },
                        onViewAll = { onContentClick("view_all_polls") }
                    )
                }
            }

            // Recommended Users Section
            item {
                RecommendedUsers(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Popular Communities Section
            item {
                PopularCommunities(
                    onGroupClick = onGroupClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Content Grid (Trending/For You)
            item {
                ContentGrid(
                    onContentClick = onContentClick,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search Field
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(24.dp)
                ),
            label = { Text("Search") },
            placeholder = { Text("Search users, content, groups...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            )
        )


        // Filter Button
        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filters",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SearchFilters(
    categories: List<String>,
    selectedCategory: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(category) },
                label = {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                },
                enabled = true,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedLeadingIconColor = Color.White,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    selectedBorderColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    selected = isSelected,
                    enabled = true
                )
            )
        }
    }
}

@Composable
private fun PollItem(
    question: String,
    options: List<String>,
    onVote: (Int) -> Unit,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    val totalVotes = remember { (50..500).random() } // Simulate random vote count
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onViewAll() }
            .padding(16.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        options.take(2).forEachIndexed { index, option ->
            val votes = remember { (totalVotes * (0.3f + Random.nextFloat() * 0.4f)).toInt() }
            val percentage = (votes.toFloat() / totalVotes * 100).toInt()
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Vote progress bar
                LinearProgressIndicator(
                    progress = votes.toFloat() / totalVotes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
            
            if (index < 1) Spacer(modifier = Modifier.height(8.dp))
        }
        
        // View all polls text
        if (options.size > 2) {
            Text(
                text = "+${options.size - 2} more options • $totalVotes votes",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End)
            )
        } else {
            Text(
                text = "$totalVotes votes",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.End)
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(80.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ContentGrid(
    onContentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentItems = remember { generateContentItems() }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidth = (screenWidth - 24.dp) / 2 // Account for padding and spacing
    val itemHeight = (itemWidth * 1.5f)
    
    // Calculate total height needed for all items
    val rowCount = (contentItems.size + 1) / 2
    val totalHeight = (itemHeight * rowCount) + (4.dp * (rowCount - 1)) + 16.dp
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = totalHeight)
    ) {
        Text(
            text = "Trending Now",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = totalHeight)
        ) {
            items(contentItems) { item ->
                ContentItem(
                    item = item,
                    onClick = { onContentClick(item.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                )
            }
        }
    }
}

@Composable
private fun ContentItem(
    item: ContentItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0.5f
                        )
                    )
            )
        }
        
        // Content info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = "${item.likes} likes • ${item.comments} comments",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun PopularCommunities(
    onGroupClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val communities = remember { generateCommunities() }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp) // Set minimum height to prevent measurement issues
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Communities",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            TextButton(onClick = { /* See all */ }) {
                Text("See All")
            }
        }
        
        // Wrap LazyRow in a Box with fixed height
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // Fixed height for the scrollable area
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(communities) { community ->
                    CommunityCard(
                        community = community,
                        onClick = { onGroupClick(community.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityCard(
    community: Community,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(120.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = community.imageUrl,
                contentDescription = community.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = community.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        
        Text(
            text = "${community.members} members",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RecommendedUsers(
    modifier: Modifier = Modifier
) {
    val users = remember { generateRecommendedUsers() }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 220.dp) // Set minimum height to prevent measurement issues
    ) {
        Text(
            text = "People You May Know",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Wrap LazyRow in a Box with fixed height
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp) // Fixed height for the scrollable area
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(users) { user ->
                    UserCard(
                        user = user,
                        onFollowClick = { isFollowing -> /* Handle follow */ },
                        modifier = Modifier.width(120.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    user: User,
    onFollowClick: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isFollowing by remember { mutableStateOf(user.isFollowing) }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(120.dp)
            .width(100.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = user.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            if (user.isVerified) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = user.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (onFollowClick != null) {
            Button(
                onClick = {
                    isFollowing = !isFollowing
                    onFollowClick(isFollowing)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFollowing) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    contentColor = if (isFollowing) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(24.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text(
                    text = if (isFollowing) "Following" else "Follow",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// Sample data functions
private fun generateContentItems(): List<ContentItem> {
    return listOf(
        ContentItem("1", "Beautiful sunset at the beach", "https://picsum.photos/400/600?random=1", 1243, 89),
        ContentItem("2", "Morning coffee vibes", "https://picsum.photos/400/600?random=2", 892, 45),
        ContentItem("3", "Weekend hiking adventure", "https://picsum.photos/400/600?random=3", 2156, 132),
        ContentItem("4", "New recipe alert!", "https://picsum.photos/400/600?random=4", 567, 34),
        ContentItem("5", "Work from home setup", "https://picsum.photos/400/600?random=5", 1789, 201),
        ContentItem("6", "Book recommendations", "https://picsum.photos/400/600?random=6", 432, 28)
    )
}

private fun generateCommunities(): List<Community> {
    return listOf(
        Community("1", "Photography Lovers", "https://picsum.photos/200/200?random=10", 12500),
        Community("2", "Fitness Enthusiasts", "https://picsum.photos/200/200?random=11", 8900),
        Community("3", "Food & Travel", "https://picsum.photos/200/200?random=12", 21500),
        Community("4", "Tech Geeks", "https://picsum.photos/200/200?random=13", 15600),
        Community("5", "Art & Design", "https://picsum.photos/200/200?random=14", 10200)
    )
}

private fun generateRecommendedUsers(): List<User> {
    return listOf(
        User("1", "Alex Johnson", "https://randomuser.me/api/portraits/men/1.jpg"),
        User("2", "Sarah Miller", "https://randomuser.me/api/portraits/women/2.jpg", true),
        User("3", "David Kim", "https://randomuser.me/api/portraits/men/3.jpg"),
        User("4", "Emma Wilson", "https://randomuser.me/api/portraits/women/4.jpg", true),
        User("5", "James Brown", "https://randomuser.me/api/portraits/men/5.jpg")
    )
}
