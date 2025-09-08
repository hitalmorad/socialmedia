package com.loop.socialmedia.ui.st

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.repository.CloudinaryRepository
import com.loop.socialmedia.ui.theme.temple_sunset_golden
import com.loop.socialmedia.ui.theme.temple_sunset_light_orange
import com.loop.socialmedia.ui.theme.temple_sunset_orange
import com.loop.socialmedia.ui.theme.temple_sunset_sky_blue
import com.loop.socialmedia.ui.theme.temple_sunset_water_blue
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoUrl by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Gradient background layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            temple_sunset_orange,
                            temple_sunset_golden,
                            temple_sunset_light_orange,
                            temple_sunset_sky_blue,
                            temple_sunset_water_blue
                        )
                    )
                )
        )

        // Subtle vignette for readability
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.08f),
                            Color.Black.copy(alpha = 0.18f)
                        )
                    )
                )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create your account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = step / 4f,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (step) {
                    1 -> {
                        // Date of Birth Picker
                        val calendar = remember { Calendar.getInstance() }
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        
                        var showDatePicker by remember { mutableStateOf(false) }
                        val dateFormatter = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
                        
                        // Date Picker Dialog
                        if (showDatePicker) {
                            val datePicker = android.app.DatePickerDialog(
                                context,
                                { _, selectedYear, selectedMonth, selectedDay ->
                                    calendar.set(selectedYear, selectedMonth, selectedDay)
                                    dateOfBirth = dateFormatter.format(calendar.time)
                                    showDatePicker = false
                                },
                                year - 18, // Default to 18 years ago
                                month,
                                day
                            )
                            datePicker.datePicker.maxDate = System.currentTimeMillis()
                            datePicker.show()
                        }
                        
                        // Date of Birth Field
                        OutlinedTextField(
                            value = dateOfBirth,
                            onValueChange = {},
                            label = { Text("Date of Birth") },
                            readOnly = true,
                            singleLine = true,
                            leadingIcon = { 
                                Icon(
                                    Icons.Default.CalendarToday, 
                                    contentDescription = "Select Date",
                                    modifier = Modifier.size(24.dp)
                                ) 
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDatePicker = true },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            singleLine = true,
                            leadingIcon = { 
                                Icon(
                                    Icons.Filled.Person, 
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                ) 
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.AlternateEmail, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            isError = errorMessage.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            isError = errorMessage.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            isError = errorMessage.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    2 -> {
                        // Circular profile image holder
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (photoUri != null) {
                                AsyncImage(
                                    model = photoUri,
                                    contentDescription = "Selected profile image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    text = "No Image",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (photoUri == null) "Select Profile Photo" else "Change Profile Photo")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Profiles with pics get 70% more connections!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    3 -> {
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Bio") },
                            maxLines = 3,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        // Date of Birth field is now at the top of the form
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("Gender") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    4 -> {
                        Text(
                            text = "Account Creation",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You're almost done! Press 'Complete Sign Up' to create your account and proceed to select your interests.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = { step-- },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Back")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Button(
                        onClick = {
                            if (step == 1) {
                                if (password != confirmPassword) {
                                    errorMessage = "Passwords do not match"
                                    return@Button
                                }
                                if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                                    errorMessage = "Please fill all fields"
                                    return@Button
                                }
                                // Basic email validation
                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    errorMessage = "Please enter a valid email address"
                                    return@Button
                                }
                                step++
                            } else if (step == 2) {
                                if (photoUri == null) {
                                    errorMessage = "Please select a profile photo"
                                    return@Button
                                }
                                coroutineScope.launch {
                                    isLoading = true
                                    try {
                                        val userId = UUID.randomUUID().toString()
                                        val result = cloudinaryRepository.uploadProfileImage(context, photoUri!!, userId)
                                        result.onSuccess { url ->
                                            photoUrl = url
                                            step++ // Move to next step only on successful upload
                                        }.onFailure { e ->
                                            errorMessage = "Image upload failed: ${e.message}"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Image upload failed: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            } else if (step == 3) {
                                if (bio.isEmpty() || dateOfBirth.isEmpty() || gender.isEmpty()) {
                                    errorMessage = "Please fill all fields"
                                    return@Button
                                }
                                // Basic date of birth validation (YYYY-MM-DD)
                                val dateRegex = Regex("""^\d{4}-\d{2}-\d{2}$""")
                                if (!dateRegex.matches(dateOfBirth)) {
                                    errorMessage = "Please enter date of birth in YYYY-MM-DD format"
                                    return@Button
                                }
                                step++
                            } else {
                                isLoading = true
                                errorMessage = ""
                                coroutineScope.launch {
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnSuccessListener { authResult ->
                                            val userId = authResult.user?.uid ?: UUID.randomUUID().toString()
                                            val userData = hashMapOf(
                                                "id" to userId,
                                                "name" to name,
                                                "username" to username,
                                                "email" to email,
                                                "profileImageUrl" to photoUrl,
                                                "bio" to bio,
                                                "dateOfBirth" to dateOfBirth,
                                                "gender" to gender,
                                                "interests" to emptyList<String>(),
                                                "followers" to emptyList<String>(),
                                                "following" to emptyList<String>(),
                                                "posts" to emptyList<String>(),
                                                "stories" to emptyList<String>(),
                                                "reels" to emptyList<String>(),
                                                "friendshipScore" to 0,
                                                "streakCount" to 0,
                                                "isVerified" to false,
                                                "isPrivate" to false,
                                                "createdAt" to System.currentTimeMillis(),
                                                "lastActive" to System.currentTimeMillis()
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
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(if (step == 4) "Complete Sign Up" else "Next")
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}