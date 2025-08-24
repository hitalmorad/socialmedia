package com.loop.socialmedia.ui.st

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun OnboardingScreen(
    firestore: FirebaseFirestore,
    auth: FirebaseAuth,
    onComplete: () -> Unit
) {
    var selectedInterests by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Interest categories with emojis
    val interestCategories = mapOf(
        "Entertainment" to listOf(
            "Music" to "🎵",
            "Movies" to "🎬",
            "TV Shows" to "📺",
            "Anime" to "🇯🇵",
            "Podcasts" to "🎙️",
            "Gaming" to "🎮",
            "K-Pop" to "💿",
            "Hip-Hop" to "🎤"
        ),
        "Creativity" to listOf(
            "Photography" to "📸",
            "Drawing/Art" to "🎨",
            "Writing" to "✍️",
            "Dance" to "💃",
            "Fashion" to "👗",
            "Design" to "🖌️",
            "DIY / Crafts" to "🛠️"
        ),
        "Lifestyle & Wellness" to listOf(
            "Fitness" to "🏋️",
            "Sports" to "⚽",
            "Yoga/Meditation" to "🧘",
            "Healthy Eating" to "🥗",
            "Travel" to "✈️",
            "Adventure" to "🏔️",
            "Nature / Hiking" to "🌳"
        ),
        "Knowledge & Learning" to listOf(
            "Tech & Gadgets" to "💻",
            "Science" to "🔬",
            "Space" to "🚀",
            "History" to "📜",
            "Books & Literature" to "📚",
            "Self-Improvement" to "🌱",
            "Coding / Programming" to "💾"
        ),
        "Social & Fun" to listOf(
            "Memes" to "😂",
            "Social Challenges" to "🏆",
            "Trending Hashtags" to "📈",
            "Personality Quizzes" to "❓",
            "Friendship Goals" to "🤝",
            "Dating & Relationships" to "💕",
            "Student Life" to "🎓"
        ),
        "Future-Oriented" to listOf(
            "Entrepreneurship" to "💼",
            "Startups" to "🚀",
            "Finance & Investing" to "💰",
            "Sustainability" to "🌍",
            "AI & Innovation" to "🤖"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Your Interests",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select at least 3 interests to continue",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
        ) {
            interestCategories.forEach { (category, interests) ->
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(interests) { (interest, emoji) ->
                        val isSelected = selectedInterests.contains(interest)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedInterests = if (isSelected) {
                                    selectedInterests - interest
                                } else {
                                    selectedInterests + interest
                                }
                            },
                            label = {
                                Text("$emoji $interest")
                            },
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                labelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedInterests.size < 3) {
                    errorMessage = "Please select at least 3 interests"
                    return@Button
                }
                isLoading = true
                errorMessage = ""
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    firestore.collection("users").document(userId)
                        .update("interests", selectedInterests)
                        .addOnSuccessListener {
                            isLoading = false
                            onComplete()
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            errorMessage = "Failed to save interests: ${e.message}"
                        }
                } else {
                    isLoading = false
                    errorMessage = "User not authenticated"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Finish")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}