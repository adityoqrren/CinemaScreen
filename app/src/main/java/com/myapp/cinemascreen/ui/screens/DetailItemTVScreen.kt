package com.myapp.cinemascreen.ui.screens

import com.myapp.cinemascreen.data.models.Creator
import com.myapp.cinemascreen.data.models.Episode
import com.myapp.cinemascreen.data.models.Network
import com.myapp.cinemascreen.data.models.TVDetail
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.myapp.cinemascreen.ui.screens.components.StudioItem
import com.myapp.cinemascreen.data.models.Crew
import com.myapp.cinemascreen.fontFamily
import com.myapp.cinemascreen.ui.screens.components.AppBarExpandedHeight
import com.myapp.cinemascreen.ui.screens.components.BottomLikeShare
import com.myapp.cinemascreen.ui.screens.components.CardWithAnimatedBorder
import com.myapp.cinemascreen.ui.screens.components.Overview
import com.myapp.cinemascreen.ui.screens.components.ParalaxToolbar
import com.myapp.cinemascreen.ui.screens.components.Studio
import com.myapp.cinemascreen.ui.screens.components.TopCasts
import com.myapp.cinemascreen.ui.screens.components.imageHeight
import com.myapp.cinemascreen.utils.getDate
import com.myapp.cinemascreen.ui.theme.CinemaScreenTheme
import com.myapp.cinemascreen.ui.states.UIstate
import com.myapp.cinemascreen.ui.theme.YellowDarker
import com.myapp.cinemascreen.ui.theme.YellowMain
import com.myapp.cinemascreen.ui.theme.YellowOrange

//val AppBarCollapsedHeight = 56.dp
//val AppBarExpandedHeight = 400.dp
//val imageHeight = AppBarExpandedHeight - AppBarCollapsedHeight

//val templateOverview =
//    "The youngest of King Triton’s daughters, and the most defiant, Ariel longs to find out more about the world beyond the sea, and while visiting the surface, falls for the dashing Prince Eric. With mermaids forbidden to interact with humans, Ariel makes a deal with the evil sea witch, Ursula, which gives her a chance to experience life on land, but ultimately places her life – and her father’s crown – in jeopardy."

@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailItemTVScreen(id: Int,
                     backToBefore: () -> Unit,
                     viewModel : DetailTVviewModel = hiltViewModel()) {
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

    LaunchedEffect(key1 = itemId) {
        //fetch to viewmodel
        viewModel.getDetailMovie(itemId)
    }

    val maxOffset =
        with(LocalDensity.current) { imageHeight.roundToPx() } - WindowInsets.statusBars.getTop(
            LocalDensity.current
        )

    val offset = derivedStateOf {
        kotlin.math.min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    }

    val tvDetail by viewModel.detailTV.collectAsStateWithLifecycle()

    var startBoxAnimation by remember {
        mutableStateOf(false)
    }

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
            when(tvDetail){
                is UIstate.Error -> {
                    val detail = tvDetail as UIstate.Error<TVDetail?>
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
                    val detail = tvDetail as UIstate.Success<TVDetail?>

                        if(detail.data != null){
                            //Text(text = detail.data.title)
                            //Log.d("check data in DetailItemScreen","data: ${detail.data}")

                            LaunchedEffect(true) {
                                Log.d("check LaunchedEffect startBoxAnimation","this is executed $startBoxAnimation")
                                startBoxAnimation = true
                            }

                            DetailItemScreenContent(
                                scrollState = scrollState,
                                detailItem = detail.data,
                                startBoxAnimation = startBoxAnimation
//                                topActors = detail.data.cast,
//                                importantCrews = importantCrews
                            )
                            ParalaxToolbar(
                                offset = offset.value,
                                maxOffset = maxOffset,
                                backToBefore = backToBefore,
                                movieTitle = detail.data.name,
                                movieImageUrl = detail.data.poster_path ?: ""
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
            BottomLikeShare(modifier = Modifier.align(Alignment.BottomCenter), isFavorite = isFavorite, onFavoriteAction = {viewModel.setFavorite(isFavorite)})
        }
    }
}

@Composable
private fun DetailItemScreenContent(
    scrollState: LazyListState,
    detailItem: TVDetail,
    startBoxAnimation: Boolean
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
                tvRating = detailItem.vote_average,
                tvPopularity = detailItem.popularity,
                tvFirstAired = detailItem.first_air_date?.let { getDate(it) }?:"",
                tvSeasons = detailItem.number_of_seasons,
                tvEpisodes = detailItem.number_of_episodes,
            )
            detailItem.overview?.let { Overview(movieOverview = it) }
            WhereToWatch(network = detailItem.networks)
            //CardWithAnimatedBorder(startAnimation = startBoxAnimation)
            CardWithAnimatedBorderCustomized(startBoxAnimation = startBoxAnimation, episode = detailItem.next_episode_to_air ?: detailItem.last_episode_to_air!!, isNext = detailItem.next_episode_to_air!=null)
            //NextLatestEpisode(episode = detailItem.next_episode_to_air ?: detailItem.last_episode_to_air!!, isNext = detailItem.next_episode_to_air!=null)
            Studio(movieStudios = detailItem.production_companies)
            InfoCrew(creators = detailItem.created_by, executiveProducers = detailItem.executiveProducers)
            if(!detailItem.cast.isNullOrEmpty()){
                TopCasts(topActors = detailItem.cast)
            }
        }
    }

}


@Composable
private fun InfoCrew(modifier: Modifier = Modifier, creators: List<Creator>, executiveProducers: List<Crew>?) {

//    val executiveProducersString = if (executiveProducers != null) {
//        importantCrews.screenplay.joinToString(",")
//    } else {
//        "No data"
//    }


    Row(
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        if(executiveProducers!=null){
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    if (executiveProducers.size > 1) "Executive Producers" else "Executive Producer",
                    fontFamily = fontFamily,
                    fontSize = 14.sp
                )
                Text(
                    executiveProducers.map { it.name }.joinToString(separator = ","),
                    fontFamily = fontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier
//                .background(Color.Green)
                .fillMaxWidth()
        ) {
            Text(if (creators.size > 1) "Creators" else "Creator",
                fontFamily = fontFamily,
                fontSize = 14.sp)
            Text(
                creators.joinToString(",") { it.name },
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
    tvRating: Double,
    tvPopularity: Double,
    tvFirstAired: String,
    tvSeasons: Int,
    tvEpisodes: Int
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
                .height(IntrinsicSize.Min)
                .padding(bottom = 12.dp)
        ) {
            Text(text = "Rating ")
            Text(
                text = tvRating.toString(),
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if(tvSeasons>1) "$tvSeasons seasons" else "$tvSeasons season",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            VerticalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight()
            )
            Text(
                text = if(tvEpisodes>1) "$tvEpisodes episodes" else "$tvEpisodes episode",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Row {
            Text(text = "Popularity ")
            Text(
                text = tvPopularity.toString(),
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Row {
            Text(text = "First aired ")
            Text(
                text = tvFirstAired,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun WhereToWatch(modifier: Modifier = Modifier, network: List<Network>){
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(text = "Where to watch", Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(network){
                    item ->
                StudioItem(nameOfStudio = item.name, imgStudio = item.logo_path ?: "" )
            }
        }
    }
}

@Composable
fun CardWithAnimatedBorderCustomized(
    startBoxAnimation: Boolean,
    episode: Episode,
    isNext: Boolean
){

    val colors = listOf(YellowMain, YellowDarker, YellowOrange)

    Row(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        CardWithAnimatedBorder(borderColors = colors, startAnimation = startBoxAnimation){
            Row {
                NextLatestEpisode(episode = episode, isNext = isNext)
            }
        }
    }

}

@Composable
private fun NextLatestEpisode(modifier: Modifier = Modifier, episode: Episode, isNext: Boolean){
//    val angleInRadians = Math.toRadians(45.0)
    Column(
        modifier = modifier
//            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
//            .border(
//                width = 2.dp,
//                shape = RoundedCornerShape(8.dp),
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        MaterialTheme.colorScheme.primary,
//                        MaterialTheme.colorScheme.tertiary
//                    ),
//                )
//            )
            .padding(all = 16.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //TODO: draw small play logo
            Canvas(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
            ){
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, size.height / 2f)
                    lineTo(0f, size.height)
                    close()
                }

                drawIntoCanvas { canvas: Canvas ->
                    canvas.drawOutline(
                        outline = Outline.Generic(path),
                        paint = Paint().apply {
                            color = YellowDarker
                            pathEffect = PathEffect.cornerPathEffect(16f)
                        }
                    )
                }
            }
            Spacer(
                Modifier.width(4.dp)
            )
            Text(text = if(isNext) "Latest episode" else  "Next Episode", style = TextStyle(color = MaterialTheme.colorScheme.tertiary), fontSize = 16.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Season ${episode.season_number}")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "episode ${episode.episode_number}", color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = episode.air_date?.let { getDate(it) } ?: "", color = MaterialTheme.colorScheme.secondary, style = TextStyle(fontSize = 14.sp))
        }
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
//            Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "load item photo",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .weight(2f)
//                    .clip(RoundedCornerShape(8.dp))
//            )
            AsyncImage(model = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/${episode.still_path}", contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier
                .weight(2f)
                .clip(
                    RoundedCornerShape(8.dp)
                ))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = episode.overview ?: "", modifier = Modifier.weight(3f))
        }
    }
}

@Preview
@Composable
fun DetailItemTVScreenPreview() {
    CinemaScreenTheme {
        DetailItemScreen(id = 0, mediaType = "movie", {})
    }
}
