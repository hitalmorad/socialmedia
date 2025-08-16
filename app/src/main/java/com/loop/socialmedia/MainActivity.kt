package com.loop.socialmedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.repository.CloudinaryRepository
import com.loop.socialmedia.ui.navigation.AppNavigation
import com.loop.socialmedia.ui.theme.LoopSocialMediaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var cloudinaryRepository: CloudinaryRepository
    @Inject lateinit var firestore: FirebaseFirestore
    @Inject lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoopSocialMediaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val isUserLoggedIn = auth.currentUser != null
                    AppNavigation(
                        navController = navController,
                        cloudinaryRepository = cloudinaryRepository,
                        firestore = firestore,
                        auth = auth,
                        isUserLoggedIn = isUserLoggedIn
                    )
                }
            }
        }
    }
}