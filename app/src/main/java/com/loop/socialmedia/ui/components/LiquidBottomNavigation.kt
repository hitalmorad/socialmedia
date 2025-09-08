package com.loop.socialmedia.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.loop.socialmedia.ui.theme.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import kotlin.math.roundToInt

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

    var containerWidthPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val pillSizeDp = 48.dp
    val pillWidthPx = with(density) { pillSizeDp.toPx() }
    val initialIndex = items.indexOfFirst { it.route == currentRoute }.coerceIn(0, items.lastIndex)
    val selectedIndexAnim = remember { Animatable(initialIndex.toFloat()) }
    LaunchedEffect(currentRoute) {
        val target = items.indexOfFirst { it.route == currentRoute }.coerceIn(0, items.lastIndex)
        selectedIndexAnim.animateTo(
            targetValue = target.toFloat(),
            animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
        )
    }
    val displayIndex = selectedIndexAnim.value.roundToInt().coerceIn(0, items.lastIndex)
    val centersPx = remember { mutableStateMapOf<Int, Float>() }
    val itemWeights = remember { mutableStateMapOf<Int, Float>() }
    val showPill = displayIndex != 2 // no pill for the center create button
    val pillTargetXPx: Float = if (containerWidthPx == 0 || !showPill) 0f else run {
        val measuredCenter = centersPx[displayIndex]
        if (measuredCenter != null) {
            // Use the actual measured center and adjust by half the pill width
            measuredCenter - pillWidthPx / 2f
        } else {
            // Fallback calculation if measurement not available yet
            val slotWidth = containerWidthPx / 5f
            (displayIndex * slotWidth + slotWidth / 2f - pillWidthPx / 2f)
        }
    }
    val animatedXPx by animateFloatAsState(
        targetValue = pillTargetXPx,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "pill_x"
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
            .onSizeChanged { containerWidthPx = it.width }
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side items (Home, Discover)
            items.take(2).forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 4.dp)
                        .onGloballyPositioned { c ->
                            centersPx[index] = c.positionInParent().x + c.size.width / 2f
                            itemWeights[index] = c.size.width.toFloat()
                        }
                ) {
                    BottomNavItem(
                        item = item,
                        isSelected = displayIndex == index,
                        onClick = { onNavigate(item.route) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Center diamond CTA button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .zIndex(4f)
                        .align(Alignment.TopCenter)
                        .offset(y = (-24).dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    temple_sunset_orange,
                                    temple_sunset_golden
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .rotate(45f)
                        .clickable { onCreateClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .rotate(-45f)
                    )
                }
            }

            // Right side items (Messages, Profile)
            items.drop(3).forEachIndexed { rIndex, item ->
                val index = rIndex + 3
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 4.dp)
                        .onGloballyPositioned { c ->
                            centersPx[index] = c.positionInParent().x + c.size.width / 2f
                            itemWeights[index] = c.size.width.toFloat()
                        }
                ) {
                    BottomNavItem(
                        item = item,
                        isSelected = displayIndex == index,
                        onClick = { onNavigate(item.route) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Sliding selected pill indicator drawn on top of the items
        if (showPill) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(animatedXPx.roundToInt(), (-10).dp.roundToPx()) }
                    .size(pillSizeDp)
                    .align(Alignment.CenterStart)
                    .background(Color.White, CircleShape)
                    .zIndex(4f),
                contentAlignment = Alignment.Center
            ) {
                val selectedItem = items[displayIndex]
                Icon(
                    imageVector = selectedItem.icon,
                    contentDescription = selectedItem.label,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
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
            .alpha(if (isSelected) 0f else 1f)
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
    }
}
