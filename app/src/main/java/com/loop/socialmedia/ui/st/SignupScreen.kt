package com.loop.socialmedia.ui.st

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.repository.CloudinaryRepository
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun SignUpScreen(
    firestore: FirebaseFirestore,
    cloudinaryRepository: CloudinaryRepository,
    auth: FirebaseAuth,
    onSignUpSuccess: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var bio by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = step / 4f,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (step) {
            1 -> {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                TextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
                TextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth") })
            }
            2 -> {
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            3 -> {
                // Photo upload component would go here
                TextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") })
                Text("Profiles with pics get 70% more connections!", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (step < 3) {
                    step++
                } else {
                    isLoading = true
                    errorMessage = ""
                    coroutineScope.launch {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener { authResult ->
                                val userId = authResult.user?.uid ?: UUID.randomUUID().toString()
                                val userData = hashMapOf(
                                    "name" to name,
                                    "username" to username,
                                    "dob" to dob,
                                    "email" to email,
                                    "bio" to bio
                                )
                                firestore.collection("users").document(userId).set(userData)
                                    .addOnSuccessListener {
                                        onSignUpSuccess()
                                        isLoading = false
                                    }
                                    .addOnFailureListener { e ->
                                        errorMessage = "Failed to save user data: ${e.message}"
                                        isLoading = false
                                    }
                            }
                            .addOnFailureListener { e ->
                                errorMessage = "Signup failed: ${e.message}"
                                isLoading = false
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(if (step == 3) "Complete Sign Up" else "Next")
            }
        }
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
        }
    }
}