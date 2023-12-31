package com.myapp.cinemascreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    toDetailScreen: (Int, String) -> Unit,
    toProfileScreen: () -> Unit,
) {
    NavHost(navController = navController,
        startDestination = BottomBarDestinations.Home.route
    ){
        composable(route = BottomBarDestinations.Home.route){
           HomeScreen(toDetailScreen = toDetailScreen, toProfileScreen = toProfileScreen)
        }
        composable(route = BottomBarDestinations.Favorites.route){
            FavoritesScreen(toDetailScreen = toDetailScreen)
        }
        composable(route = BottomBarDestinations.Find.route){
            FindScreen(onNavigateUp = {navController.navigateUp()})
        }
    }
}