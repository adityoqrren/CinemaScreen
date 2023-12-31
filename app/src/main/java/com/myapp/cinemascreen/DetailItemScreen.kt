package com.myapp.cinemascreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.myapp.cinemascreen.data.Cast
import com.myapp.cinemascreen.data.ImportantCrews
import com.myapp.cinemascreen.data.MovieDetail
import com.myapp.cinemascreen.data.ProductionCompany
import com.myapp.cinemascreen.ui.theme.CinemaScreenTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

val AppBarCollapsedHeight = 56.dp
val AppBarExpandedHeight = 400.dp
val imageHeight = AppBarExpandedHeight - AppBarCollapsedHeight

//val templateOverview =
//    "The youngest of King Triton’s daughters, and the most defiant, Ariel longs to find out more about the world beyond the sea, and while visiting the surface, falls for the dashing Prince Eric. With mermaids forbidden to interact with humans, Ariel makes a deal with the evil sea witch, Ursula, which gives her a chance to experience life on land, but ultimately places her life – and her father’s crown – in jeopardy."

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItemScreen(id: Int, mediaType: String, backToBefore: () -> Unit) {
    val scrollState = rememberLazyListState()

    val generateMovieDetail =
        generateMovieDetail("datajson/moviedetail_littlemermaid.json", LocalContext.current)
    val movieDetail by remember { mutableStateOf(generateMovieDetail) }

    val generateTopActors =
        generateTopActors("datajson/credit_littlemermaid.json", 7, LocalContext.current)
    val topActors by remember {
        mutableStateOf(generateTopActors)
    }

    val generateImportantCrews =
        generateImportantCrews("datajson/credit_littlemermaid.json", LocalContext.current)
    val importantCrews by remember {
        mutableStateOf(generateImportantCrews)
    }

    val maxOffset =
        with(LocalDensity.current) { imageHeight.roundToPx() } - WindowInsets.statusBars.getTop(
            LocalDensity.current
        )

    val offset = derivedStateOf {
        kotlin.math.min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    }

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
            DetailItemScreenContent(
                scrollState = scrollState,
                detailItem = movieDetail,
                topActors = topActors,
                importantCrews = importantCrews
            )
            ParalaxToolbar(
                offset = offset.value,
                maxOffset = maxOffset,
                backToBefore = backToBefore,
                movieTitle = movieDetail.title
            )
            BottomLikeShare(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun BottomLikeShare(modifier: Modifier = Modifier) {
    var isClicked by remember { mutableStateOf(false) }
    //code above using var because we want to change the value. it uses delegated property so it directly forward to mutablestate.value
    var isLoved by remember {
        mutableStateOf(false)
    }

    val interactionSource = remember { MutableInteractionSource() }
//    val loveIconColor by animateColorAsState(if(isLoved) Color.Red else Color.White)

//    val updatedIsClicked by rememberUpdatedState(isClicked)

    val coroutineScope = rememberCoroutineScope()

    val loveScaleFactor by animateFloatAsState(
        targetValue = if (isClicked) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        shadowElevation = 2.dp,
        modifier = modifier.padding(bottom = 20.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                )
                .padding(7.dp)
                .fillMaxWidth(0.5f)
                .wrapContentHeight()
        ) {
            Box(
                modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.mdi_share),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .background(Color.White)
                    .width(1.dp)
                    .height(46.dp)
            )
            Box(
                modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
//                IconButton(onClick = {
//                    isLoved = !isLoved
//                    if(isLoved){
//                        isClicked = !isClicked
//                        coroutineScope.launch{
//                            delay(500)
//                            isClicked = false
//                        }
//                    }
//                }, modifier = Modifier
////                    .background(Color.Green)
//                    .padding(0.dp)) {
                Icon(
                    imageVector = if (isLoved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isLoved) Color.Red else Color.White,
                    modifier = Modifier
//                            .background(Color.Red)
                        .height(32.dp)
                        .width(32.dp)
                        .scale(loveScaleFactor)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            isLoved = !isLoved
                            if (isLoved) {
                                isClicked = !isClicked
                                coroutineScope.launch {
                                    delay(500)
                                    isClicked = false
                                }
                            }
                        }
                )
//                }
            }
        }
    }
}

@Composable
fun SetStatusBarColor(color: Color, useDarkIcons: Boolean) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = useDarkIcons
        )

        onDispose { }
    }
}

@Composable
fun ParalaxToolbar(offset: Int, maxOffset: Int, backToBefore: () -> Unit, movieTitle: String) {
    ExpandedAppBar(offset = offset, maxOffset = maxOffset, movieTitle = movieTitle)
    StaticButtonAppbar(offset = offset, maxOffset = maxOffset, backToBefore = backToBefore)
}

@Composable
fun StaticButtonAppbar(offset: Int, maxOffset: Int, backToBefore: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppBarHeight)
            .zIndex(5f)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier
                .height(24.dp)
                .width(24.dp)
                .clickable { backToBefore() },
            tint = if (offset > maxOffset / 2) MaterialTheme.colorScheme.primary else Color.White
        )
    }
}

@Composable
fun ExpandedAppBar(offset: Int, maxOffset: Int, movieTitle: String) {

    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset

    if (offset > (maxOffset / 2)) {
        SetStatusBarColor(color = Color.Transparent, useDarkIcons = true)
    } else {
        SetStatusBarColor(color = Color.Transparent, useDarkIcons = false)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppBarExpandedHeight)
            .offset({ IntOffset(x = 0, y = -offset) }),
        color = Color.White,
        shadowElevation = if (offset == maxOffset) 4.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .height(imageHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = 1f - offsetProgress
                        }
                ) {
                    //image
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w1280/8FQeHmusSN2hk3bICf16x5GFQvT.jpg",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    //layer blurry white in front of image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colorStops = arrayOf(
                                        Pair(0.02f, Color.Transparent),
                                        Pair(1f, Color.White)
                                    )
                                )
                            )
                    )
                }
                Row(
                    modifier = Modifier
//                        .background(Color.Blue)
                        .fillMaxWidth()
                        .height(AppBarCollapsedHeight),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movieTitle,
                        maxLines = if (offset > maxOffset / 2) 1 else 2,
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier
                            .scale(1f - 0.2f * offsetProgress)
                            .padding(
                                start = 16.dp,
                                end = if (offset > maxOffset) 30.dp else 16.dp
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun DetailItemScreenContent(
    scrollState: LazyListState,
    detailItem: MovieDetail,
    topActors: List<Cast>,
    importantCrews: ImportantCrews
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
            InfoCrew(importantCrews = importantCrews)
            TopCasts(topActors = topActors)
        }
    }

}

@Composable
fun TopCasts(modifier: Modifier = Modifier, topActors: List<Cast>) {
    Log.d("see topActors", "$topActors")

    Column(modifier = modifier.padding(top = 16.dp)) {
        Text("Top Cast", Modifier.padding(horizontal = 16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            items(topActors) { actor ->
                TopCastItem(
                    actorImg = actor.profile_path ?: "",
                    actorName = actor.name,
                    actorCharacter = actor.character ?: ""
                )
            }
        }
    }
}

@Composable
fun InfoCrew(modifier: Modifier = Modifier, importantCrews: ImportantCrews) {

    val headingWritingDepartment: String = if (importantCrews.screenplay != null) {
        if (importantCrews.screenplay.size > 0) "Screenplays" else "Screenplay"
    } else {
        if (importantCrews.writers!!.size > 0) "Writers" else "Writer"
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
fun Studio(modifier: Modifier = Modifier, movieStudios: List<ProductionCompany>) {
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text("Studio", Modifier.padding(horizontal = 16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = 16.dp)
        ) {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(movieStudios) { item ->
                    StudioItem(
                        nameOfStudio = item.name,
                        imgStudio = if (item.logo_path == null) "" else item.logo_path
                    )
                }
            }
        }
    }
}

@Composable
fun Overview(modifier: Modifier = Modifier, movieOverview: String) {
    Column(modifier = modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Text(text = "Overview")
        Text(
            text = movieOverview,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            fontFamily = fontFamily,
            fontSize = 12.sp
        )
    }
}

@Composable
fun InfoBox(
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
