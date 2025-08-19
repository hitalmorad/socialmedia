package com.loop.socialmedia.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.ai.client.generativeai.Chat
import com.loop.socialmedia.ui.theme.*

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun LiquidBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("discover", Icons.Default.Search, "Discover"),
        BottomNavItem("create", Icons.Default.Add, "Create"),
        BottomNavItem("messages", Icons.Default.Chat, "Messages"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .zIndex(1000f)
    ) {
        // Liquid background effect
        val liquidScale by animateFloatAsState(
            targetValue = if (currentRoute == "create") 1.2f else 1.0f,
            animationSpec = tween(300),
            label = "liquid_scale"
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp)
                .scale(liquidScale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            temple_sunset_orange.copy(alpha = 0.8f),
                            temple_sunset_golden.copy(alpha = 0.6f)
                        )
                    ),
                    CircleShape
                )
                .zIndex(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side items (Home, Discover)
            items.take(2).forEach { item ->
                BottomNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Center Create button
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .zIndex(2f)
                    .clickable { onCreateClick() }
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                temple_sunset_orange,
                                temple_sunset_golden
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Right side items (Messages, Profile)
            items.drop(3).forEach { item ->
                BottomNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        animationSpec = tween(200),
        label = "item_scale"
    )

    Column(
        modifier = modifier
            .clickable { onClick() }
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = if (isSelected) temple_sunset_orange else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) temple_sunset_orange else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
