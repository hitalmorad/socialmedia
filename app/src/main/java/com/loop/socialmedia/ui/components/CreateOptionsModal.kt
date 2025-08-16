package com.loop.socialmedia.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.loop.socialmedia.ui.theme.md_theme_light_primary
import com.loop.socialmedia.ui.theme.md_theme_light_secondary

data class CreateOption(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun CreateOptionsModal(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onCreatePost: () -> Unit,
    onCreateStory: () -> Unit,
    onCreateReel: () -> Unit,
    onMoreOptions: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(
            animationSpec = tween(300),
            initialScale = 0.8f
        ),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(
            animationSpec = tween(300),
            targetScale = 0.8f
        )
    ) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Text(
                        text = "Create",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Main options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CreateOptionItem(
                            option = CreateOption(
                                title = "Post",
                                icon = Icons.Default.Image,
                                color = md_theme_light_primary,
                                onClick = onCreatePost
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        CreateOptionItem(
                            option = CreateOption(
                                title = "Story",
                                icon = Icons.Default.Circle,
                                color = md_theme_light_secondary,
                                onClick = onCreateStory
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        CreateOptionItem(
                            option = CreateOption(
                                title = "Reel",
                                icon = Icons.Default.VideoLibrary,
                                color = Color(0xFF10B981),
                                onClick = onCreateReel
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // More options button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onMoreOptions() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "More",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "More Options",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Close button
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateOptionItem(
    option: CreateOption,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(200),
        label = "option_scale"
    )

    Column(
        modifier = modifier
            .clickable { option.onClick() }
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            option.color.copy(alpha = 0.2f),
                            option.color.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = option.title,
                tint = option.color,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = option.title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
