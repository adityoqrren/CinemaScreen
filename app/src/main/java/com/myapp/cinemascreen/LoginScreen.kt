package com.myapp.cinemascreen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit, navigateToRegister: () -> Unit
) {
    var isCardVisible by remember { mutableStateOf(false) }


    LaunchedEffect(true) {
        delay(100)
        isCardVisible = true
    }

    Surface(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding(), color = MaterialTheme.colorScheme.primary
    ) {

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }
        var isPasswordShow by remember { mutableStateOf(false) }

        var valueOfUsername by remember { mutableStateOf("") }
        var valueOfPassword by remember { mutableStateOf("") }

        var isUsernameError by remember { mutableStateOf(false) }
        var isPasswordError by remember { mutableStateOf(false) }

        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.curvy_left_shape),
                contentDescription = "curvy shape",
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                contentScale = ContentScale.FillBounds
            )
            BoxWithConstraints(
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .clickable(
                        interactionSource = interactionSource, indication = null
                    ) {
                        keyboardController?.hide()
                        focusManager.clearFocus(true)
                    }) {
                val offsetYtoMove = (maxHeight * 0.75f)
                val offsetYtoMovePx =
                    with(LocalDensity.current) { offsetYtoMove.toPx().roundToInt() }
                val cardHeight = (maxHeight * 0.75f) + 20.dp
                val offsetYNotMovePx = with(LocalDensity.current) { 20.dp.toPx().roundToInt() }

                val offset by animateIntOffsetAsState(
                    targetValue = if (!isCardVisible) {
                        IntOffset(0, offsetYtoMovePx)
                    } else {
                        IntOffset(0, offsetYNotMovePx)
//                       IntOffset.Zero
                    }, label = "offset", animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )

                Column {
                    Text(
                        text = "Welcome Back",
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp)
                    )
                    Text(
                        text = "You can login with username or email to see movies and tv shows updates and save your favorite ones.",
                        fontFamily = fontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Box(modifier = Modifier
                    .offset { offset }
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .height(cardHeight)
//                    .fillMaxHeight(0.75f)
                    .fillMaxWidth()
                    .zIndex(4f)
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)) {
                    val focusRequester = remember { FocusRequester() }

                    Column(Modifier.fillMaxWidth()) {
                        //Username Input
                        FormTextField(value = valueOfUsername,
                            onValueChange = {
                                valueOfUsername = it
                                isUsernameError = false
                            },
                            errorIndicator = isUsernameError,
                            onErrorChange = { boolean -> isUsernameError = boolean },
                            validatorFunction = { value -> value.isNotBlank() && value.isNotEmpty() },
                            placeHolderText = "Username or Email",
                            errorText = "username or email is not valid",
                            focusManager = focusManager,
                            modifier = Modifier
                                .padding(top = 56.dp)
                                .fillMaxWidth()
                        )
                        //Password Input
                        FormPasswordTextField(value = valueOfPassword,
                            onValueChange = {
                                valueOfPassword = it
                                isPasswordError = false
                            },
                            errorIndicator = isPasswordError,
                            onErrorChange = { boolean -> isPasswordError = boolean },
                            validatorFunction = { password -> password.isNotEmpty() && password.isNotBlank() },
                            placeHolderText = "Password",
                            errorText = "Password is not valid",
                            focusManager = focusManager,
                            isInTheEnd = true,
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Forgot Password?",
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        }

                        Button(
                            onClick = {
                                if (valueOfUsername.isEmpty() && valueOfUsername.isBlank()) {
                                    isUsernameError = true
                                }
                                if (valueOfPassword.isEmpty() && valueOfPassword.isBlank()) {
                                    isPasswordError = true
                                }
                                if (!isUsernameError && !isPasswordError) {
                                    navigateToHome()
                                }

                            },
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Login")
                        }

                        Row(
                            modifier = Modifier
                                .padding(36.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Don’t have an account? ",
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(text = "Sign Up",
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.clickable {
                                    navigateToRegister()
                                })
                        }
                    }

                }
            }
        }
    }
}

//@Preview(device = Devices.PIXEL_4)
//@Composable
//fun LoginScreenPrev() {
//    LoginScreen()
//}