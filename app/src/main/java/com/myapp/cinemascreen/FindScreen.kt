package com.myapp.cinemascreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myapp.cinemascreen.data.MovieTVListItem

@Composable
fun FindScreen(
    viewModel: FindViewModel = hiltViewModel(),
    onNavigateUp : () -> Unit
) {

    val genres = viewModel.genres
    val listRecentSearchResults by viewModel.recentSearch.collectAsStateWithLifecycle()

    var isFirstBackPress by remember { mutableStateOf(true) }

    var buttonVisibility by remember {
        mutableStateOf(true)
    }

    val focusManager = LocalFocusManager.current

    BackHandler {
        if(isFirstBackPress){
            isFirstBackPress = false
            focusManager.clearFocus(true)
            viewModel.clearSearchValue()
        }else{
            onNavigateUp()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(top = 180.dp)) {
            item {
//                Row(
//                    modifier = Modifier
//                        .background(Color.Green)
//                        .fillMaxWidth()
//                        .height(IntrinsicSize.Min)
//                        .padding(20.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .background(MaterialTheme.colorScheme.tertiary)
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .padding(20.dp),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        Text("Ini dia namanya", textAlign = TextAlign.Center)
//                    }
//                    Box(
//                        modifier = Modifier
//                            .background(MaterialTheme.colorScheme.tertiary)
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .padding(20.dp),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        Text(
//                            "Ini dia namanya yang agak panjang awukwukwukwukwuk",
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                }
                GenresBox(genreData = genres)
                //button to expand genres
                AnimatedVisibility(
                    visible = buttonVisibility, exit = slideOutVertically() + fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                viewModel.expandingGenres()
                                buttonVisibility = false
                            }, colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = MaterialTheme.colorScheme.secondary
                            ), modifier = Modifier.defaultMinSize(
                                minWidth = ButtonDefaults.MinWidth, minHeight = 1.dp
                            ), contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(text = "See More")
                        }
                    }
                }
                //recent search results
                RecentSearchResults(listRecentSearchResults = listRecentSearchResults)
            }
        }
        FindScreenToolbar(searchValue = viewModel.searchValue,
            onChangeSearchValue = { newSearchValue -> viewModel.toSetSearchValue(newSearchValue) },
            onClearSearch = { viewModel.clearSearchValue() },
            focusManager = focusManager)
    }
}

@Composable
fun RecentSearchResults(
    listRecentSearchResults: List<MovieTVListItem>, modifier: Modifier = Modifier
) {
    Column(
        modifier.padding(bottom = 16.dp)
    ) {
        Row(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Recent Search Results",
                modifier = Modifier.padding(bottom = 12.dp),
                style = TextStyle(
                    fontFamily = fontFamily, fontSize = 16.sp
                )
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            items(listRecentSearchResults) { item ->
                PosterCardPopular(item = item)
            }
        }
    }
}

@Composable
fun GenresBox(genreData: List<String>, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Text(
            text = "Genres", modifier = Modifier.padding(bottom = 12.dp), style = TextStyle(
                fontFamily = fontFamily, fontSize = 16.sp
            )
        )
        Box(
            modifier = Modifier.animateContentSize()
        ) {
            EasyGrid(
                columnCount = 2,
                paddingValues = PaddingValues(0.dp),
                verticalSpace = 8.dp,
                horizontalSpace = Arrangement.spacedBy(8.dp),
                list = genreData,
            ) { genre ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.tertiary)
                        .clickable { }
                        .height(80.dp)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        genre, textAlign = TextAlign.Center, style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FindScreenToolbar(
    searchValue: String,
    onChangeSearchValue: (String) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
    focusManager: FocusManager
) {
//    val localContext = LocalContext.current
//    val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

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
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        ) {
            TextField(value = searchValue,
                onValueChange = { onChangeSearchValue(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                placeholder = { Text("Find movie or TV show") },
                leadingIcon = {
                    if (searchValue.isNotEmpty()) {
                        IconButton(onClick = {
                            onClearSearch()
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Outlined.ArrowBack, contentDescription = "icon clear search")
                        }
                    } else {
//                        IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Search, contentDescription = "icon search")
//                        }
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        if (searchValue.isNotEmpty()) {
                            IconButton(onClick = onClearSearch) {
                                Icon(Icons.Outlined.Clear, contentDescription = "icon clear search")
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_mic_none_24),
                                contentDescription = "icon open mic"
                            )
                        }
                    }
                })
        }
    }
}

//@Composable
//fun <T> EasyGridWithHeight(
//    columnCount: Int,
//    paddingValues: PaddingValues,
//    verticalSpace: Dp,
//    horizontalSpace: Arrangement.Horizontal,
//    list: List<T>,
//    content: @Composable (T) -> Unit,
//) {
//    Column(
//        modifier = Modifier.padding(paddingValues),
//        verticalArrangement = Arrangement.spacedBy(space = verticalSpace)
//    ) {
//        for (i in list.indices step columnCount) {
//            Row(
//                horizontalArrangement = horizontalSpace,
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                for (j in 0 until columnCount) {
//
//                    if ((i + j) < list.size) {
//                        Box(
//                            modifier = Modifier
//                                .weight(1f)
////                                .height(),
//                        ) {
//                            content(list[i + j])
//                        }
//                    } else {
//                        Spacer(modifier = Modifier.weight(1f, fill = true))
//                    }
//
//                }
//            }
//        }
//    }
//}
