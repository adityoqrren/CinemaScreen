package com.myapp.cinemascreen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = CinemaScreenDestinations.LoginScreen.route
    ) {
        composable(
            route = CinemaScreenDestinations.LoginScreen.route,
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
        ) {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(CinemaScreenDestinations.Main.route) {
                        popUpTo(route = CinemaScreenDestinations.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToRegister = { navController.navigate(CinemaScreenDestinations.RegisterScreen.route) }
            )
        }
        composable(route = CinemaScreenDestinations.RegisterScreen.route) {
            RegisterScreen(
                navigateToLogin = { navController.navigate(CinemaScreenDestinations.LoginScreen.route) },
                popAfterSuccess = { navController.popBackStack() })
        }
        composable(route = CinemaScreenDestinations.Main.route) {
            MainScreen(toDetailScreen = { id, mediaType -> navController.navigate("detailItemScreen/$id/$mediaType") },
                toProfileScreen = { navController.navigate(CinemaScreenDestinations.ProfileScreen.route) })
        }
        composable(route = CinemaScreenDestinations.DetailItemScreen.route,
            arguments = listOf(navArgument("itemID") { type = NavType.IntType },
                navArgument("mediatype") { type = NavType.StringType }
            ),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(400)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(400)
                )
            }
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("itemID")
                ?.let {
                    backStackEntry.arguments?.getString("mediatype")
                        ?.let { it1 ->
                            DetailItemScreen(
                                id = it,
                                mediaType = it1,
                                backToBefore = { navController.popBackStack() })
                        }
                }
        }
        composable(route = CinemaScreenDestinations.ProfileScreen.route) {
            ProfileScreen(
                backToBefore = { navController.popBackStack() },
                logoutToLoginScreen = {
                    navController.navigate(CinemaScreenDestinations.LoginScreen.route) {
                        popUpTo(CinemaScreenDestinations.ProfileScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}