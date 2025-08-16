package com.loop.socialmedia.ui.st

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun OnboardingScreen(
    firestore: FirebaseFirestore,
    onComplete: () -> Unit
) {
    var selectedInterests by remember { mutableStateOf(listOf<String>()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Your Interests", style = MaterialTheme.typography.headlineMedium)
        // Interest selection chips would go here
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Save interests to Firestore
                onComplete()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish")
        }
    }
}