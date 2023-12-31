package com.myapp.cinemascreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarDestinations(
    val route: String,
    val title: String,
    val icon: ImageVector
){
    object Home: BottomBarDestinations(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Favorites: BottomBarDestinations(
        route = "favorites",
        title = "Favorites",
        icon = Icons.Default.Favorite
    )
    object Find: BottomBarDestinations(
        route = "find",
        title = "Find",
        icon = Icons.Default.Search
    )
}