package com.myapp.cinemascreen.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.myapp.cinemascreen.utils.MediaType
import com.myapp.cinemascreen.Resource
import com.myapp.cinemascreen.data.CinemaScreenRepository
import com.myapp.cinemascreen.data.models.MovieDetail
import com.myapp.cinemascreen.ui.states.UIstate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val cinemaScreenRepository: CinemaScreenRepository
) : ViewModel() {

    private val _detailMovie = MutableStateFlow<UIstate<MovieDetail?>>(UIstate.Loading)
    val detailMovie : StateFlow<UIstate<MovieDetail?>> get() = _detailMovie

//    private val _showShimmer = MutableStateFlow<Boolean>(false)
//    val showSimmer : StateFlow<Boolean> get() = _showShimmer

    private var _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage : StateFlow<String?> get() = _errorMessage

    private val _isRefresh = MutableStateFlow<Boolean>(false)
    val isRefresh : StateFlow<Boolean> get() = _isRefresh

    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    fun getDetailMovie(mediaType: String, movieId: Int){
        viewModelScope.launch {
            _detailMovie.value = UIstate.Loading
//            setShowSimmer(true)
            val detailMovie =  cinemaScreenRepository.getDetailMovie(mediaType, movieId)
            detailMovie.collect {
                    data ->
                when(data){
                    is Resource.Success -> {
                        _detailMovie.value = UIstate.Success(data.data as MovieDetail)
                        _isRefresh.value = false
                        checkFavorite(auth.uid!!,data.data.id)
                    }
                    is Resource.Error -> {
//                        withContext(Dispatchers.Main){
                        Log.d("check getDetailMovie() in vm","error message: ${data.message}")
//                        }
                        _detailMovie.value = UIstate.Error(message = data.message as String, dataWhenError = data.data, errorCode = 1)
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
        when(detailMovie.value){
            is UIstate.Error -> {}
            is UIstate.Loading -> {}
            is UIstate.Success -> {
                val detail = detailMovie.value as UIstate.Success<MovieDetail?>
                if(detail.data!=null){
                    if(!isFavorite){
                        //save data to firestore favorites collection as document
                        val data = hashMapOf(
                            "id" to detail.data.id,
                            "title" to detail.data.title,
                            "poster_path" to detail.data.poster_path,
                            "media_type" to MediaType.Movie
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
                                Log.w("setFavorite check","setFavorite movie save data to firestore failed")
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
                                Log.w("setFavorite check","setFavorite movie remove data from firestore failed")
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

    fun setRefresh(boolean: Boolean, mediaType: String, movieId: Int){
        _isRefresh.value = boolean
        if(_isRefresh.value){
            getDetailMovie(mediaType, movieId)
        }
    }
}