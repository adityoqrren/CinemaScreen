package com.myapp.cinemascreen.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.myapp.cinemascreen.data.models.UserRegistration
import com.myapp.cinemascreen.ui.states.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel(){
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private var _registerState = MutableStateFlow<RegisterEvent>(RegisterEvent())
    val registerState get() : StateFlow<RegisterEvent> = _registerState

    fun createUser(userRegistration: UserRegistration){
        _registerState.value = _registerState.value.copy(isLoading = true)
        auth = Firebase.auth

        //save to firebase authentication (email, password)
        auth.createUserWithEmailAndPassword(userRegistration.email, userRegistration.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("user registration", "createUserWithEmail:success")
                    val userID = auth.currentUser?.uid
                    //save user data to firebase firestore
                    if(!userID.isNullOrEmpty()){
                        saveUserDataFirestore(userRegistration,userID)
                    }else{
                        _registerState.value = _registerState.value.copy(isLoading = false, isRegisterSuccess = false, errorMessage = "there is a problem when creating")
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("user registration", "createUserWithEmail:failure", task.exception)
                    _registerState.value = _registerState.value.copy(isLoading = false, isRegisterSuccess = false, errorMessage = "there is a problem when creating ${task.exception}")
                }
            }
    }

    private fun saveUserDataFirestore(userRegistration: UserRegistration, uid: String){
        val data = hashMapOf(
            "username" to userRegistration.username,
            "fullname" to userRegistration.fullname,
            "country" to userRegistration.selectedCountry
        )

        db.collection("users")
            .document(uid)
            .set(data)
            .addOnSuccessListener { documentReference ->
                Log.d("saveUserDataFirestore check", "DocumentSnapshot added with ID: $uid")
                //log out user, we don't want user logged in before they login my themselves
                auth.signOut()
                _registerState.value = _registerState.value.copy(isLoading = false, isRegisterSuccess = true, errorMessage = null)
            }
            .addOnFailureListener {
                 e -> Log.w("saveUserDataFirestore check", "Error writing document", e)
                _registerState.value = _registerState.value.copy(isLoading = false, isRegisterSuccess = false, errorMessage = "Error Adding Document")
                //remove user from authentication
                val user = auth.currentUser
                user?.delete()
                Log.d("saveUserDataFirestore check", "user deleted: ${user?.uid}")
            }
    }

    fun setErrorMessage(message: String?){
        _registerState.value = _registerState.value.copy(errorMessage = message)
    }
}