package com.loop.socialmedia.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.loop.socialmedia.ui.theme.md_theme_light_primary
import com.loop.socialmedia.ui.theme.md_theme_light_secondary

@Composable
fun CreateOptionsModal(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    navController: NavController,
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
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CreateOptionItem(
                            title = "Post",
                            icon = Icons.Default.Image,
                            color = md_theme_light_primary,
                            onClick = {
                                onDismiss()
                                navController.navigate("create_post")
                            }
                        )

                        CreateOptionItem(
                            title = "Story",
                            icon = Icons.Default.Circle,
                            color = md_theme_light_secondary,
                            onClick = {
                                onDismiss()
                                navController.navigate("create_story")
                            }
                        )

                        CreateOptionItem(
                            title = "Reel",
                            icon = Icons.Default.VideoLibrary,
                            color = MaterialTheme.colorScheme.tertiary,
                            onClick = {
                                onDismiss()
                                navController.navigate("create_reel")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                onDismiss()
                                navController.navigate("more_options")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("More Options")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateOptionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(title, fontWeight = FontWeight.Medium)
    }
}
