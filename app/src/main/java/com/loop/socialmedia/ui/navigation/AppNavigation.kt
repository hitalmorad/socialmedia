package com.loop.socialmedia.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.repository.CloudinaryRepository
import com.loop.socialmedia.ui.components.CreateOptionsModal
import com.loop.socialmedia.ui.components.LiquidBottomNavigation
import com.loop.socialmedia.ui.screens.*
import com.loop.socialmedia.ui.st.LoginScreen
import com.loop.socialmedia.ui.st.OnboardingScreen
import com.loop.socialmedia.ui.st.SignUpScreen
import com.loop.socialmedia.ui.st.SplashScreen
import com.loop.socialmedia.ui.st.WelcomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    cloudinaryRepository: CloudinaryRepository,
    firestore: FirebaseFirestore,
    auth: FirebaseAuth,
    isUserLoggedIn: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var currentRoute = navBackStackEntry?.destination?.route ?: if (isUserLoggedIn) "home" else "splash"
    var showCreateModal by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf("splash", "welcome", "login", "signup", "onboarding", "post_preview")) {
                LiquidBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        currentRoute = route
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onCreateClick = { showCreateModal = true }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isUserLoggedIn) "home" else "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
            // --- Auth & Onboarding ---
            composable("splash") {
                SplashScreen(
                    isUserLoggedIn = isUserLoggedIn,
                    onNavigate = {
                        val target = if (isUserLoggedIn) "home" else "welcome"
                        navController.navigate(target) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
            composable("welcome") {
                WelcomeScreen(
                    onSignUpClick = { navController.navigate("signup") },
                    onLoginClick = { navController.navigate("login") },
                    onGuestClick = { navController.navigate("home") }
                )
            }
            composable("login") {
                LoginScreen(
                    firestore = firestore,
                    auth = auth,
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onSignUpClick = { navController.navigate("signup") }
                )
            }
            composable("signup") {
                SignUpScreen(
                    firestore = firestore,
                    cloudinaryRepository = cloudinaryRepository,
                    auth = auth,
                    onSignUpSuccess = { navController.navigate("onboarding") }
                )
            }
            composable("onboarding") {
                OnboardingScreen(
                    firestore = firestore,
                    auth = FirebaseAuth.getInstance(),
                    onComplete = {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // --- Main Tabs ---
            composable("home") {
                HomeScreen(onCreateClick = { showCreateModal = true })
            }
            composable("discover") { DiscoverScreen() }
            composable("messages") { MessagesScreen() }
            composable("profile") {
                ProfileScreen()
            }

            // --- Create Screens ---
            composable("create_post") {
                PostEditorScreen(navController = navController)
            }
            composable("create_story") {
                CreateStoryScreen(navController = navController)
            }
            composable("create_reel") {
                CreateReelScreen(navController = navController)
            }
            composable("more_options") {
                MoreOptionsScreen(navController = navController)
            }

            // --- Preview Screen ---
            composable("post_preview") {
                PostPreviewScreen(
                    navController = navController,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // --- Create Modal ---
        CreateOptionsModal(
            isVisible = showCreateModal,
            onDismiss = { showCreateModal = false },
            navController = navController,
            onCreatePost = {
                showCreateModal = false
                navController.navigate("create_post")
            },
            onCreateStory = {
                showCreateModal = false
                navController.navigate("create_story")
            },
            onCreateReel = {
                showCreateModal = false
                navController.navigate("create_reel")
            },
            onMoreOptions = {
                showCreateModal = false
                navController.navigate("more_options")
            }
        )
    }
}