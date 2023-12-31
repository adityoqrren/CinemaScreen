package com.myapp.cinemascreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.myapp.cinemascreen.data.MovieTVListItem

@Composable

fun FavoritesScreen(
    toDetailScreen: (Int, String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val categoryChoosen by viewModel.idSelected.collectAsStateWithLifecycle()
    val favoritesMovieTV by viewModel.favoritesMovieTV.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(top = 140.dp)) {
            item {
                FavoritesCategoryButtons(
                    categoryChoosen = categoryChoosen,
                    onChangeCategory = { categoryCode: Int -> viewModel.setIdSelected(categoryCode) },
                )
                //FavoritesList(data = favoritesMovieTV)
            }
            item{
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
            }
            GridList(
                columnCount = 2,
                verticalSpace = 8.dp,
                horizontalSpace = 8.dp,
                horizontalPadding = 16.dp,
                favoritesMovieTV){ item ->
                PosterCardExpanding(item = item)
            }
        }
        FavoriteTopToolbar()
    }
}

fun <T>LazyListScope.GridList(
    columnCount : Int,
    verticalSpace: Dp,
    horizontalSpace: Dp,
    horizontalPadding: Dp,
    list: List<T>,
    content: @Composable (T) -> Unit,
) {
    for (i in list.indices step columnCount) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(horizontalSpace),
                modifier = Modifier
                    .padding(bottom = verticalSpace, start = horizontalPadding, end = horizontalPadding)
                    .fillMaxWidth()
            ) {
                for (j in 0 until columnCount) {

                    if ((i + j) < list.size) {
                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            content(list[i + j])
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f, fill = true))
                    }

                }
            }
        }
    }
}

@Composable
fun FavoritesList(data: List<MovieTVListItem>) {
    EasyGrid(
        columnCount = 2,
        paddingValues = PaddingValues(16.dp),
        verticalSpace = 8.dp,
        horizontalSpace = Arrangement.spacedBy(8.dp),
        list = data
    ) { item ->
        PosterCardExpanding(item = item)
    }
}

@Composable
fun FavoritesCategoryButtons(
    modifier: Modifier = Modifier,
    categoryChoosen: Int,
    onChangeCategory: (Int) -> Unit
) {
    Row(
        modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = { onChangeCategory(1) },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            shape = ButtonDefaults.outlinedShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (categoryChoosen == 1) Color.White else MaterialTheme.colorScheme.secondary,
                containerColor = if (categoryChoosen == 1) MaterialTheme.colorScheme.secondary else Color.White
            ),
            modifier = Modifier.defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = 1.dp
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(text = "All")
        }

        OutlinedButton(
            onClick = { onChangeCategory(2) },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            shape = ButtonDefaults.outlinedShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (categoryChoosen == 2) Color.White else MaterialTheme.colorScheme.secondary,
                containerColor = if (categoryChoosen == 2) MaterialTheme.colorScheme.secondary else Color.White
            ),
            modifier = Modifier.defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = 1.dp
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(text = "Movies")
        }

        OutlinedButton(
            onClick = { onChangeCategory(3) },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            shape = ButtonDefaults.outlinedShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (categoryChoosen == 3) Color.White else MaterialTheme.colorScheme.secondary,
                containerColor = if (categoryChoosen == 3) MaterialTheme.colorScheme.secondary else Color.White
            ),
            modifier = Modifier.defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = 1.dp
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(text = "TV Shows")
        }
    }
}

@Composable
fun FavoriteTopToolbar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
                .height(AppBarHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CinemaScreen",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                style = TextStyle(
                    fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp
                ),
                color = Color.White
            )
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp),
                tint = Color.White
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "20 movies and tv shows", color = Color.White)
        }
    }

}

@Composable
fun <T> EasyGrid(
    columnCount: Int,
    paddingValues: PaddingValues,
    verticalSpace: Dp,
    horizontalSpace: Arrangement.Horizontal,
    list: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    Column(
        modifier = modifier.padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(space = verticalSpace)
    ) {
        for (i in list.indices step columnCount) {
            Row(
                horizontalArrangement = horizontalSpace,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (j in 0 until columnCount) {

                    if ((i + j) < list.size) {
                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            content(list[i + j])
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f, fill = true))
                    }

                }
            }
        }
    }
}

@Composable
fun PosterCardExpanding(item: MovieTVListItem) {
    //Log.d("see PosterCard item", "${item.title} and ${item.poster_path}")
    Card(
        modifier = Modifier.aspectRatio(2/3f)
    ){
        Box {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/original/${item.poster_path}",
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}
