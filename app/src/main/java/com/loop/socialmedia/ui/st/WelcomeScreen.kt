package com.loop.socialmedia.ui.st

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.loop.socialmedia.ui.theme.*

@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onGuestClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Beautiful temple sunset gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            temple_sunset_orange, // Warm orange sunset
                            temple_sunset_golden, // Golden orange
                            temple_sunset_light_orange, // Light orange
                            temple_sunset_sky_blue, // Sky blue
                            temple_sunset_water_blue  // Deep blue water
                        )
                    )
                )
        )
        
        // Floating particles effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            // Floating particles
            repeat(8) { index ->
                val offset = (index * 45).dp
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = offset, y = (index * 60).dp)
                        .size(4.dp)
                        .background(
                            Color.White.copy(alpha = 0.6f),
                            CircleShape
                        )
                )
            }
            
            repeat(6) { index ->
                val offset = (index * 70).dp
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-offset), y = (index * 80).dp)
                        .size(3.dp)
                        .background(
                            Color.White.copy(alpha = 0.4f),
                            CircleShape
                        )
                )
            }
        }
        
        // Temple silhouette decoration
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ) {
            // Temple base (water reflection effect)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                temple_sunset_water_blue.copy(alpha = 0.8f),
                                temple_sunset_water_blue.copy(alpha = 0.6f),
                                temple_sunset_water_blue.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
            
            // Temple main structure with more detail
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-100).dp)
                    .size(120.dp, 200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                temple_brown, // Brown temple color
                                temple_brown_light
                            )
                        ),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )
            
            // Temple entrance
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-80).dp)
                    .size(40.dp, 60.dp)
                    .background(
                        temple_dark_brown,
                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
            )
            
            // Temple roof with more detail
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-180).dp)
                    .size(80.dp, 60.dp)
                    .background(
                        temple_dark_brown,
                        RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
            )
            
            // Temple spire
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-220).dp)
                    .size(20.dp, 40.dp)
                    .background(
                        temple_brown,
                        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
            )
            
            // Temple decorative elements
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(
                            x = (-40 + (index * 40)).dp,
                            y = (-140).dp
                        )
                        .size(8.dp, 8.dp)
                        .background(
                            temple_brown_light,
                            CircleShape
                        )
                )
            }
        }
        
        // Additional overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(3f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
        )
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .zIndex(4f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with app name and tagline
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App name - "LOOP" in large stylized font
                Text(
                    text = "LOOP",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Call to action text
                Text(
                    text = "Don't wait.",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Get best experience now!",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
            
            // Bottom section with buttons
            Column(
                modifier = Modifier.padding(bottom = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Login button (large white button)
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Log in",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sign up button (dark button with white border)
                OutlinedButton(
                    onClick = onSignUpClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.8f)
                    ),
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Don't have an Account? Sign Up",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                }


                Spacer(modifier = Modifier.height(24.dp))
                
                // Guest option (optional)
                TextButton(
                    onClick = onGuestClick,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Continue as Guest",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }
    }
}
