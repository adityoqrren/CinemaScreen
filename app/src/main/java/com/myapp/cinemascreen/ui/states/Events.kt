package com.myapp.cinemascreen.ui.states

data class RegisterEvent(
    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val errorMessage: String? = null,
)

data class LoginEvent(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

data class LogoutEvent(
    val isLoading: Boolean = false,
    val isLogoutSuccess: Boolean = false,
    val errorMessage: String? = null,
)

