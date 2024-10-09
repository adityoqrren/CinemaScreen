package com.myapp.cinemascreen.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myapp.cinemascreen.data.models.ImportantCrews
import com.myapp.cinemascreen.data.models.MovieDetail
import com.myapp.cinemascreen.fontFamily
import com.myapp.cinemascreen.ui.screens.components.AppBarExpandedHeight
import com.myapp.cinemascreen.ui.screens.components.BottomLikeShare
import com.myapp.cinemascreen.ui.screens.components.Overview
import com.myapp.cinemascreen.ui.screens.components.ParalaxToolbar
import com.myapp.cinemascreen.ui.screens.components.Studio
import com.myapp.cinemascreen.ui.screens.components.TopCasts
import com.myapp.cinemascreen.ui.screens.components.imageHeight
import com.myapp.cinemascreen.utils.getDuration
import com.myapp.cinemascreen.utils.getMovieYear
import com.myapp.cinemascreen.ui.theme.CinemaScreenTheme
import com.myapp.cinemascreen.ui.states.UIstate

//val templateOverview =
//    "The youngest of King Triton’s daughters, and the most defiant, Ariel longs to find out more about the world beyond the sea, and while visiting the surface, falls for the dashing Prince Eric. With mermaids forbidden to interact with humans, Ariel makes a deal with the evil sea witch, Ursula, which gives her a chance to experience life on land, but ultimately places her life – and her father’s crown – in jeopardy."

@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailItemScreen(id: Int, mediaType: String,
                     backToBefore: () -> Unit,
                     viewModel : DetailViewModel = hiltViewModel()) {
    val scrollState = rememberLazyListState()
//
//    val generateMovieDetail =
//        generateMovieDetail("datajson/moviedetail_littlemermaid.json", LocalContext.current)
//    val movieDetail by remember { mutableStateOf(generateMovieDetail) }
//
//    val generateTopActors =
//        generateTopActors("datajson/credit_littlemermaid.json", 7, LocalContext.current)
//    val topActors by remember {
//        mutableStateOf(generateTopActors)
//    }
//
//    val generateImportantCrews =
//        generateImportantCrews("datajson/credit_littlemermaid.json", LocalContext.current)
//    val importantCrews by remember {
//        mutableStateOf(generateImportantCrews)
//    }

    val itemId by remember{ mutableIntStateOf(id) }
    val itemMediaType by remember {
        mutableStateOf(mediaType)
    }

    LaunchedEffect(key1 = itemId) {
        //fetch to viewmodel
        viewModel.getDetailMovie(itemMediaType, itemId)
    }

    val maxOffset =
        with(LocalDensity.current) { imageHeight.roundToPx() } - WindowInsets.statusBars.getTop(
            LocalDensity.current
        )

    val offset = derivedStateOf {
        kotlin.math.min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    }

    val movieDetail by viewModel.detailMovie.collectAsStateWithLifecycle()

    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(top = 0.dp)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize()
        ) {
//            Text(text = "Ini halaman detail $id - type $mediaType")
            when(movieDetail){
                is UIstate.Error -> {
                    val detail = movieDetail as UIstate.Error<MovieDetail?>
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "error: ${detail.message}")
                    }
                }
                is UIstate.Loading -> {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UIstate.Success -> {
                    val detail = movieDetail as UIstate.Success<MovieDetail?>

                        if(detail.data != null){
                            //Text(text = detail.data.title)
                            //Log.d("check data in DetailItemScreen","data: ${detail.data}")
                            DetailItemScreenContent(
                                scrollState = scrollState,
                                detailItem = detail.data,
//                                topActors = detail.data.cast,
//                                importantCrews = importantCrews
                            )
                            ParalaxToolbar(
                                offset = offset.value,
                                maxOffset = maxOffset,
                                backToBefore = backToBefore,
                                movieTitle = detail.data.title,
                                movieImageUrl = detail.data.poster_path
                            )
                        }else{
                            Box(
                                modifier = Modifier
                                    .background(Color.LightGray)
                                    .height(100.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "No data")
                            }
                        }

                }
            }
            BottomLikeShare(modifier = Modifier.align(Alignment.BottomCenter), isFavorite = isFavorite, onFavoriteAction = {
                viewModel.setFavorite(isFavorite)
            })
        }
    }
}

@Composable
private fun DetailItemScreenContent(
    scrollState: LazyListState,
    detailItem: MovieDetail,
//    topActors: List<Cast>,
//    importantCrews: ImportantCrews
) {

    LazyColumn(
        state = scrollState,
//        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(top = AppBarExpandedHeight, bottom = 90.dp)
    ) {
        item {
            InfoBox(
                movieYear = getMovieYear(detailItem.release_date),
                movieRating = detailItem.vote_average,
                moviePopularity = detailItem.popularity,
                movieDuration = getDuration(detailItem.runtime)
            )
            Overview(movieOverview = detailItem.overview)
            Studio(movieStudios = detailItem.production_companies)
            if(detailItem.importantCrews!=null){
                InfoCrew(importantCrews = detailItem.importantCrews)
            }
            if(!detailItem.cast.isNullOrEmpty()){
                TopCasts(topActors = detailItem.cast)
            }
        }
    }

}

@Composable
private fun InfoCrew(modifier: Modifier = Modifier, importantCrews: ImportantCrews) {

    val headingWritingDepartment: String = if (importantCrews.screenplay != null) {
        if (importantCrews.screenplay.size > 1) "Screenplays" else "Screenplay"
    } else {
        if (importantCrews.writers!!.size > 1) "Writers" else "Writer"
    }

    val peopleInWriting = if (importantCrews.screenplay != null) {
        importantCrews.screenplay.joinToString(",")
    } else {
        importantCrews.writers!!.joinToString(",")
    }


    Row(
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
//            .background(Color.Red)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
//                .background(Color.Magenta)
                .fillMaxWidth(0.5f)
        ) {
            Text(
                if (importantCrews.directors.size > 0) "Directors" else "Director",
                fontFamily = fontFamily,
                fontSize = 14.sp
            )
            Text(
                importantCrews.directors.joinToString(","),
                fontFamily = fontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Column(
            modifier = Modifier
//                .background(Color.Green)
                .fillMaxWidth()
        ) {
            Text(headingWritingDepartment, fontFamily = fontFamily, fontSize = 14.sp)
            Text(
                peopleInWriting,
                fontFamily = fontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun InfoBox(
    modifier: Modifier = Modifier,
    movieYear: String,
    movieRating: Double,
    moviePopularity: Double,
    movieDuration: String
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = movieYear,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(text = movieDuration)
        }
        Row(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(text = "Rating ")
            Text(
                text = movieRating.toString(),
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Row {
            Text(text = "Popularity ")
            Text(
                text = moviePopularity.toString(),
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun DetailItemScreenPreview() {
    CinemaScreenTheme {
        DetailItemScreen(id = 0, mediaType = "movie", {})
    }
}
