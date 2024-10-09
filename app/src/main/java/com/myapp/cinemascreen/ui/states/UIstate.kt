package com.myapp.cinemascreen.ui.states

sealed class UIstate<out T> {
    object Loading : UIstate<Nothing>()
    data class Success<T>(val data: T) : UIstate<T>()
    data class Error<T>(val message: String, val dataWhenError: T?, val errorCode: Int? = null) : UIstate<T>()
}