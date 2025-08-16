package com.loop.socialmedia.ui.st

import androidx.compose.animation.fadeIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.loop.socialmedia.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isUserLoggedIn: Boolean,
    onNavigate: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(isUserLoggedIn) {
        visible = true
        delay(2000)
        onNavigate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFF6200EE), Color(0xFF3700B3)))),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    text = "Connect. Play. Discover.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }
    }
}