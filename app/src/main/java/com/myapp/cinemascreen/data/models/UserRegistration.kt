package com.myapp.cinemascreen.data.models

data class UserRegistration(
    val email: String,
    val fullname: String,
    val username: String,
    val selectedCountry: String,
    val password: String
)
