package com.myapp.cinemascreen.ui.screens

import com.myapp.cinemascreen.data.models.TVDetail
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.myapp.cinemascreen.utils.MediaType
import com.myapp.cinemascreen.Resource
import com.myapp.cinemascreen.data.CinemaScreenRepository
import com.myapp.cinemascreen.ui.states.UIstate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTVviewModel @Inject constructor(
    private val cinemaScreenRepository: CinemaScreenRepository
) : ViewModel() {

    private val _detailTV = MutableStateFlow<UIstate<TVDetail?>>(UIstate.Loading)
    val detailTV : StateFlow<UIstate<TVDetail?>> get() = _detailTV

    //private val _showShimmer = MutableStateFlow<Boolean>(false)

    private var _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage : StateFlow<String?> get() = _errorMessage

    private val _isRefresh = MutableStateFlow<Boolean>(false)
    val isRefresh : StateFlow<Boolean> get() = _isRefresh

    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    fun getDetailMovie(movieId: Int){
        viewModelScope.launch {
            _detailTV.value = UIstate.Loading
//            setShowSimmer(true)
            val detailMovie =  cinemaScreenRepository.getDetailTV(movieId)
            detailMovie.collect {
                    data ->
                when(data){
                    is Resource.Success -> {
                        _detailTV.value = UIstate.Success(data.data as TVDetail)
                        _isRefresh.value = false
                        checkFavorite(auth.uid!!, data.data.id)
                    }
                    is Resource.Error -> {
//                        withContext(Dispatchers.Main){
                        Log.d("check getDetailMovie() in vm","error message: ${data.message}")
//                        }
                        _detailTV.value = UIstate.Error(message = data.message as String, dataWhenError = data.data, errorCode = 1)
                        _errorMessage.value = data.message
                        _isRefresh.value = false
                        Log.d("check error message","error message $_errorMessage")
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun checkFavorite(uid: String, id: Int){
        viewModelScope.launch {
            db.collection("users")
                .document(uid)
                .collection("favorites")
                .document(id.toString())
                .get()
                .addOnSuccessListener {
                        document ->
                    val data = document.data
                    _isFavorite.value = data!=null
                }
                .addOnFailureListener {
                    _isFavorite.value = false
                }
        }
    }

    fun setFavorite(isFavorite: Boolean){
        when(detailTV.value){
            is UIstate.Error -> {}
            is UIstate.Loading -> {}
            is UIstate.Success -> {
                val detail = detailTV.value as UIstate.Success<TVDetail?>
                if(detail.data!=null){
                    if(!isFavorite){
                        //save data to firestore favorites collection as document
                        val data = hashMapOf(
                            "id" to detail.data.id,
                            "title" to detail.data.name,
                            "poster_path" to detail.data.poster_path,
                            "media_type" to MediaType.TV
                        )

                        db.collection("users")
                            .document(auth.uid!!)
                            .collection("favorites")
                            .document(detail.data.id.toString())
                            .set(data)
                            .addOnSuccessListener {
                                checkFavorite(auth.uid!!,detail.data.id)
                            }
                            .addOnFailureListener {
                                    error ->
                                Log.w("setFavorite check","setFavorite TV save data to firestore failed")
                            }
                    }else{
                        //remove data as document from firestore favorites collection
                        db.collection("users")
                            .document(auth.uid!!)
                            .collection("favorites")
                            .document(detail.data.id.toString())
                            .delete()
                            .addOnSuccessListener {
                                checkFavorite(auth.uid!!,detail.data.id)
                            }
                            .addOnFailureListener {
                                    error ->
                                Log.w("setFavorite check","setFavorite TV remove data from firestore failed")
                            }
                    }
                }
            }
        }
    }

//    fun setShowSimmer(isShimmer: Boolean){
//        _showShimmer.value = isShimmer
//    }

    fun triggerErrorMessage(message: String?){
        _errorMessage.value = message
    }

    fun setRefresh(boolean: Boolean, movieId: Int){
        _isRefresh.value = boolean
        if(_isRefresh.value){
            getDetailMovie(movieId)
        }
    }
}