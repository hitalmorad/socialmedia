package com.loop.socialmedia.ui.st

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.loop.socialmedia.R

@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onGuestClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Image(
            painter = painterResource(id = R.drawable.feed_privew),
            contentDescription = "Feed Preview",
            modifier = Modifier
                .fillMaxSize()
                .blur(8.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Sign Up", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Login", style = MaterialTheme.typography.titleLarge)
            }

            }
        }
    }
