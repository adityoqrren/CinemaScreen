package com.myapp.cinemascreen

sealed class CinemaScreenDestinations(
    val route: String,
    val title: String,
){
    object Main: CinemaScreenDestinations(
        route = "main",
        title = "Main"
    )

    object DetailItemScreen: CinemaScreenDestinations(
        route = "detailItemScreen/{itemID}/{mediatype}",
        title = "Detail Item Screen"
    )

    object LoginScreen: CinemaScreenDestinations(
        route = "loginscreen",
        title = "Login Screen"
    )

    object RegisterScreen: CinemaScreenDestinations(
        route = "registerscreen",
        title = "Register Screen"
    )

    object ProfileScreen: CinemaScreenDestinations(
        route = "profilescreen",
        title = "Profile Screen"
    )

}
