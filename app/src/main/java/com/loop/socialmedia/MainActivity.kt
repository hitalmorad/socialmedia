package com.loop.socialmedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.repository.CloudinaryRepository
import com.loop.socialmedia.ui.navigation.AppNavigation
import com.loop.socialmedia.ui.theme.LoopSocialMediaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.OnBackPressedCallback

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
                    
                    // Handle back press for Instagram-like navigation
                    BackPressHandler(navController = navController)
                    
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

@Composable
fun BackPressHandler(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    
    DisposableEffect(navController) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestination = navController.currentDestination?.route
                when (currentDestination) {
                    "home" -> {
                        // Exit app when on home
                        activity?.finish()
                    }
                    "discover", "messages", "profile" -> {
                        // Navigate to home from other main tabs
                        navController.navigate("home") {
                            popUpTo("home") { 
                                inclusive = true 
                            }
                        }
                    }
                    "create_post", "create_story", "create_reel", "more_options" -> {
                        // Go back to home from create screens
                        navController.navigate("home") {
                            popUpTo("home") { 
                                inclusive = true 
                            }
                        }
                    }
                    else -> {
                        // Default back behavior for other screens (auth, etc.)
                        if (!navController.popBackStack()) {
                            activity?.finish()
                        }
                    }
                }
            }
        }
        
        activity?.onBackPressedDispatcher?.addCallback(callback)
        
        onDispose {
            callback.remove()
        }
    }
}