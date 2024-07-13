package com.example.catlistapp.navigation

import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catlistapp.accountDetails.accountDetailsScreen
import com.example.catlistapp.cats.details.catDetails
import com.example.catlistapp.cats.gallery.catGallery
import com.example.catlistapp.cats.gallery.photo.catPhotoScreen
import com.example.catlistapp.cats.list.cats
import com.example.catlistapp.edit.editScreen
import com.example.catlistapp.leaderboard.leaderboardScreen
import com.example.catlistapp.profile.datastore.ProfileDataStore
import com.example.catlistapp.profile.login
import com.example.catlistapp.quiz.ui.quiz
import kotlinx.coroutines.launch


@Composable
fun CatNavigation(profileDataStore: ProfileDataStore) {
    val navController = rememberNavController()

    val isProfileEmpty by profileDataStore.isEmpty.collectAsState(initial = true)
    var startDest by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            startDest = if (isProfileEmpty) "login" else "cats"
        }
    }

    startDest?.let { startDestination ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = spring(),
                    initialOffsetX = { it },
                )
            },
            exitTransition = { scaleOut(targetScale = 0.75f) },
            popEnterTransition = { scaleIn(initialScale = 0.75f) },
            popExitTransition = { slideOutHorizontally { it } }
        ) {

            login(
                route = "login",
                onCreate = {
                    navController.navigate(route = "cats")
                }
            )
            cats(
                route = "cats",
                onCatClick = { catId ->
                    navController.navigate(route = "cat_details/$catId")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onLeaderboardClick = {categoryId ->
                    navController.navigate(route = "leaderboard/$categoryId")
                }
            )
            catDetails(
                route = "cat_details/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                }),
                navController = navController,
                onGalleryButtonClick = {
                    navController.navigate(route = "cat_details/gallery/$it")
                }
            )

            catGallery(
                route = "cat_details/gallery/{id}",
                arguments = listOf(
                    navArgument(name = "id") {
                        nullable = false
                        type = NavType.StringType
                    }
                ),
                onPhotoClick = { id, photoIndex ->
                    navController.navigate(route = "photo/${id}/${photoIndex}")
                },
                onClose = {
                    navController.navigateUp()
                }
            )

            catPhotoScreen(
                route = "photo/{id}/{photoIndex}",
                navController = navController,
                arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                }, navArgument("photoIndex") {
                    type = NavType.IntType
                }),
            )
            leaderboardScreen(
                route = "leaderboard/{category}",
                navController = navController,
                arguments = listOf(
                    navArgument("category") {
                        type = NavType.IntType
                    }
                )
            )

            accountDetailsScreen(
                route = "profile",
                navController = navController,
                onBack = {
                   navController.navigateUp()
                },
                onEditProfile = {
                    navController.navigate(route = "edit_profile")
                }
            )

            editScreen(
                route = "edit_profile",
                navController = navController,
                onBack = {
                    navController.navigateUp()
                }
            )

            quiz(
                route = "quiz",
                onQuizCompleted = {
                    navController.navigate(route = "cats")
                },
                onClose = {
                    navController.navigate(route = "cats")
                }
            )

        }
    }
}

