package com.myapp.cinemascreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.myapp.cinemascreen.data.UserRegistration

class RegisterViewModel : ViewModel(){
    private lateinit var auth: FirebaseAuth

    fun createUser(userRegistration: UserRegistration){

    }
}