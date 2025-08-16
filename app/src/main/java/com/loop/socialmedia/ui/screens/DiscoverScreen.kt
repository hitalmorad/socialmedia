package com.loop.socialmedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loop.socialmedia.ui.theme.md_theme_light_primary
import com.loop.socialmedia.ui.theme.md_theme_light_secondary

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Search Bar
        item {
            SearchBar()
        }

        // Trending Hashtags
        item {
            TrendingHashtags()
        }

        // Challenges
        item {
            ChallengesSection()
        }

        // Friend Matching
        item {
            FriendMatchingSection()
        }

        // Compliments Mode
        item {
            ComplimentsSection()
        }

        // Groups & Communities
        item {
            GroupsSection()
        }
    }
}

@Composable
private fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = {
            Text("Search users, hashtags, topics...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = md_theme_light_primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
private fun TrendingHashtags() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Trending Hashtags",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getTrendingHashtags()) { hashtag ->
                TrendingHashtagCard(hashtag = hashtag)
            }
        }
    }
}

@Composable
private fun TrendingHashtagCard(hashtag: String) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable { /* Handle hashtag click */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            md_theme_light_primary.copy(alpha = 0.1f),
                            md_theme_light_secondary.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$hashtag",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_primary
            )
        }
    }
}

@Composable
private fun ChallengesSection() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Daily Challenges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getChallenges()) { challenge ->
                ChallengeCard(challenge = challenge)
            }
        }
    }
}

@Composable
private fun ChallengeCard(challenge: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .clickable { /* Handle challenge click */ },
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
                text = challenge,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun FriendMatchingSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Find New Friends",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Swipe to discover new connections",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle friend matching */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Find Friends",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Matching")
            }
        }
    }
}

@Composable
private fun ComplimentsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Send Compliments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Spread positivity anonymously",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle compliments */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Send Compliment",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send Compliment")
            }
        }
    }
}

@Composable
private fun GroupsSection() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Groups & Communities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getGroups()) { group ->
                GroupCard(group = group)
            }
        }
    }
}

@Composable
private fun GroupCard(group: String) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(100.dp)
            .clickable { /* Handle group click */ },
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
                text = group,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Sample data functions
private fun getTrendingHashtags(): List<String> {
    return listOf("Photography", "Travel", "Food", "Fitness", "Art", "Music")
}

private fun getChallenges(): List<String> {
    return listOf("Photo Challenge", "Story Challenge", "Reel Challenge", "Poll Challenge")
}

private fun getGroups(): List<String> {
    return listOf("Photography Lovers", "Travel Enthusiasts", "Food Bloggers", "Fitness Community")
}
