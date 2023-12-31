package com.myapp.cinemascreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.myapp.cinemascreen.ui.theme.CinemaScreenTheme

@Composable
fun TopCastItem(actorImg: String, actorName: String, actorCharacter: String) {
    Card(
        modifier = Modifier
            .width(108.dp)
            .aspectRatio(2 / 3f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/$actorImg",
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    actorName,
                    fontFamily = fontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
//                    style = TextStyle(
//                        platformStyle = PlatformTextStyle(
//                            includeFontPadding = false
//                        )
//                    )
                )
                Text(
                    actorCharacter, fontFamily = fontFamily, fontSize = 12.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun StudioItem(nameOfStudio: String, imgStudio: String) {
    //https://image.tmdb.org/t/p/original/wdrCwmRnLFJhEoH8GSfymY85KHT.png
    Card(
        modifier = Modifier
            .height(64.dp)
            .width(160.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = if (imgStudio.isNotEmpty()) "https://image.tmdb.org/t/p/original/$imgStudio" else R.drawable.ic_launcher_background,
                contentDescription = null,
                modifier = Modifier
                    .height(36.dp)
                    .width(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Text(nameOfStudio, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Preview
@Composable
fun ItemsPreview() {
    CinemaScreenTheme {
        Column {
            StudioItem("The walt disney studio of north america", "wdrCwmRnLFJhEoH8GSfymY85KHT.png")
            TopCastItem("acOAv6ijsYjLb8p1IyUtdZTgwKC.jpg", "Haille Bailey", "Mermaid")
        }
    }
}