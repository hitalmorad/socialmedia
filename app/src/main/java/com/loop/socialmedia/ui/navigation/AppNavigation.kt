package com.loop.socialmedia.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
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
            if (currentRoute !in listOf("splash", "welcome", "login", "signup", "onboarding")) {
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
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        WelcomeScreen(
                            onSignUpClick = {
                                navController.navigate("signup")
                            },
                            onLoginClick = {
                                navController.navigate("login")
                            },
                            onGuestClick = {
                                navController.navigate("home")
                            }
                        )
                    }
                }
            }
            composable("login") {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        LoginScreen(
                            firestore = firestore,
                            auth = auth,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onSignUpClick = {
                                navController.navigate("signup")
                            }
                        )
                    }
                }
            }
            composable("signup") {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        SignUpScreen(
                            firestore = firestore,
                            cloudinaryRepository = cloudinaryRepository,
                            auth = auth,
                            onSignUpSuccess = {
                                navController.navigate("onboarding")
                            }
                        )
                    }
                }
            }
            composable("onboarding") {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        OnboardingScreen(
                            firestore = firestore,
                            onComplete = {
                                navController.navigate("home") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
            composable("home") {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        HomeScreen(
                            onCreateClick = { showCreateModal = true }
                        )
                    }
                }
            }
            composable("discover") {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        DiscoverScreen()
                    }
                }
            }
            composable("messages") {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        MessagesScreen()
                    }
                }
            }
            composable("profile") {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth } with
                                slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth }
                    }
                ) { targetRoute ->
                    key(targetRoute) {
                        ProfileScreen(
                           // auth = auth,
                           // onLogoutClick = {
                            //    auth.signOut()
                            //    navController.navigate("welcome") {
                            //        popUpTo(0) { inclusive = true }
                            //    }
                           // }
                        )
                    }
                }
            }
        }

        CreateOptionsModal(
            isVisible = showCreateModal,
            onDismiss = { showCreateModal = false },
            onCreatePost = {
                showCreateModal = false
                // Navigate to create post screen
            },
            onCreateStory = {
                showCreateModal = false
                // Navigate to create story screen
            },
            onCreateReel = {
                showCreateModal = false
                // Navigate to create reel screen
            },
            onMoreOptions = {
                showCreateModal = false
                // Navigate to more options screen
            }
        )
    }
}