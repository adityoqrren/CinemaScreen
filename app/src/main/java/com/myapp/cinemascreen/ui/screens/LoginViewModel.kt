package com.myapp.cinemascreen.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.myapp.cinemascreen.data.CinemaScreenRepository
import com.myapp.cinemascreen.ui.UserPreferences
import com.myapp.cinemascreen.ui.states.LoginEvent
import com.myapp.cinemascreen.ui.states.LogoutEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val repository: CinemaScreenRepository
) : ViewModel(){
    /*
    * State using LoginEvent
    * Assign with object creation: _loginState.value = LoginEvent(isLoading = value, errorMessage = value)
    * */

    private val auth = FirebaseAuth.getInstance()

    private var _loginState = MutableStateFlow<LoginEvent>(LoginEvent())
    val loginState get() : StateFlow<LoginEvent> = _loginState

    private var _logoutState = MutableStateFlow<LogoutEvent>(LogoutEvent())
    val logoutState get() : StateFlow<LogoutEvent> = _logoutState

    private var _isLogin = MutableStateFlow<Boolean>(false)
    val isLogin get() : StateFlow<Boolean> = _isLogin
    private var _emailLogin = MutableStateFlow<String>("")
    val emailLogin get() : StateFlow<String> = _emailLogin
    private var _usernameLogin = MutableStateFlow<String>("")
    val usernameLogin get() : StateFlow<String> = _usernameLogin

    init {
        Log.d("check loginviewmodel","initialized")
        viewModelScope.launch {
            checkLogin()
        }
    }

    fun isLoginValue(): Boolean{
        return isLogin.value
    }

    fun login(email: String, password: String){
        _loginState.value = LoginEvent(isLoading = true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginViewModel", "signInWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        saveToPreferences(it.email.toString(),getUserInfo(it.uid))
                    }
                }else{
                    Log.w("LoginViewModel", "signInWithEmail:failure", task.exception)
                    _loginState.value = LoginEvent(isLoading = false, errorMessage = "error")
                }
            }
    }

    fun getUserInfo(uid: String) : String{
        val db = Firebase.firestore
        var username = ""

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                document ->
                if(document != null){
                    val data = document.data
                    username = data?.get("username") as String
                }else{
                    Log.d("LoginViewModel", "getUserInfo:error NOT FOUND")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("LoginViewModel", "getUserInfo:error ${exception.printStackTrace()}")
            }

        return username
    }

    private fun saveToPreferences(email: String, username: String){
        viewModelScope.launch {
            userPreferences.saveUserData(email = email, username = username, isLogin = true)
            // Run checkLogin and wait for it to finish
//            val checkLoginJob = async {
                checkLogin()
//            }
//            checkLoginJob.await()
            _loginState.value = LoginEvent(isLoading = false, errorMessage = null)
        }
    }

    suspend fun checkLogin(){
        Log.d("loginviewmodel","checkLogin() method")

//        viewModelScope.launch {
            val isLoginNow = userPreferences.isLogin.first()
            val emailLogin = userPreferences.emailLogin.first()
            val usernameLogin = userPreferences.usernameLogin.first()
            _isLogin.value = isLoginNow
            _emailLogin.value = emailLogin
            _usernameLogin.value = usernameLogin
            Log.d("check checkLogin: ","isLogin value ${_isLogin.value}")
//        }
    }

    fun setErrorMessage(msg : String?){
        _loginState.value = LoginEvent(isLoading = false, errorMessage = msg)
        _logoutState.value = LogoutEvent(isLoading = false, errorMessage = msg)
    }

    fun logout(){
        _logoutState.value = LogoutEvent(isLoading = true)

        viewModelScope.launch {
            //reset data in datastore preferences
            userPreferences.saveUserData(false,"","")
            //logout in firebase auth
            auth.signOut()
            //update check in
//            val checkLoginJob = async {
                checkLogin()
//            }
//            checkLoginJob.await()
            Log.d("check logout()","must be after await()")
            //send event
            if(!_isLogin.value){
                repository.deleteAllMovieTVSaved()
                _logoutState.value = LogoutEvent(isLoading = false, isLogoutSuccess = true)
            }else{
                _logoutState.value = LogoutEvent(isLoading = false, isLogoutSuccess = false, errorMessage = "Logout error")
            }
        }
    }

    fun forgotPassword(){

    }

}
