package com.myapp.cinemascreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.cinemascreen.ui.theme.BlackText2
import com.myapp.cinemascreen.ui.theme.LightGrey
import com.myapp.cinemascreen.ui.theme.YellowMain

@Composable
fun ProfileScreen(
    backToBefore: () -> Unit,
    logoutToLoginScreen: () -> Unit
) {
    Surface(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding(),
        color = Color.White
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(YellowMain)
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .statusBarsPadding()
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 16.dp, start = 24.dp)
                        .height(24.dp)
                        .width(24.dp)
                        .align(
                            Alignment.TopStart
                        )
                        .clickable (
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = null
                        ){
                            backToBefore()
                        }

                )

                // vector decoration

                Image(
                    painter = painterResource(id = R.drawable.circle_vector_1),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(
                            Alignment.BottomStart
                        )
                )

                Image(
                    painter = painterResource(id = R.drawable.circle_vector_2),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(
                            Alignment.BottomStart
                        )
                )

                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture),
                        contentDescription = "profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.width(28.dp))
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Adityo Aji Pambudi",
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "adityostarr",
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = BlackText2
                        )
                        Text(
                            text = "adityoajipam@gmail.com",
                            fontFamily = fontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = BlackText2
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Indonesia", modifier = Modifier.padding(end = 12.dp))
                            OutlinedButton(
                                onClick = {},
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                shape = ButtonDefaults.outlinedShape,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier.defaultMinSize(
                                    minWidth = ButtonDefaults.MinWidth,
                                    minHeight = 1.dp
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(text = "change country")
                                Icon(
                                    Icons.Default.KeyboardArrowRight,
                                    contentDescription = "icon button right"
                                )
                            }
                        }
                    }
                }

            }
            Column(
                modifier = Modifier
                    .offset(y = -50.dp)
                    .fillMaxWidth()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    ProfileCardElements(
                        icon = IconProfileCardElements.IconByVector(Icons.Default.Person),
                        name = "Person"
                    ) {}
                    Divider(
                        color = LightGrey
                    )
                    ProfileCardElements(
                        icon = IconProfileCardElements.IconByDrawable(R.drawable.material_security_rounded_32),
                        name = "Security Settings"
                    ) {}
                    Divider(
                        color = LightGrey
                    )
                    ProfileCardElements(
                        icon = IconProfileCardElements.IconByDrawable(R.drawable.material_live_help_rounded_32),
                        name = "Help Center"
                    ) {}
                    Divider(
                        color = LightGrey
                    )
                    ProfileCardElements(
                        icon = IconProfileCardElements.IconByDrawable(R.drawable.quill_paper_32),
                        name = "Terms & Conditions"
                    ) {}
                    Divider(
                        color = LightGrey
                    )
                    ProfileCardElements(
                        icon = IconProfileCardElements.IconByDrawable(R.drawable.material_privacy_tip_rounded_32),
                        name = "Privacy Policy"
                    ) {}
                }
                OutlinedButton(
                    onClick = {
                        logoutToLoginScreen()
                    },
                    border = BorderStroke(1.dp, Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red,
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(top = 40.dp, bottom = 20.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Log Out")
                }
            }
        }
    }
}

sealed class IconProfileCardElements() {
    data class IconByDrawable(val id: Int) : IconProfileCardElements()
    data class IconByVector(val imageVector: ImageVector) : IconProfileCardElements()
}

@Composable
fun ProfileCardElements(icon: IconProfileCardElements, name: String, navigateFunction: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        when (icon) {
            is IconProfileCardElements.IconByDrawable -> {
                Icon(
                    painter = painterResource(id = icon.id),
                    contentDescription = name,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp)
                )
            }

            is IconProfileCardElements.IconByVector -> {
                Icon(
                    imageVector = icon.imageVector,
                    contentDescription = name,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(text = name, fontFamily = fontFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal)
    }
}