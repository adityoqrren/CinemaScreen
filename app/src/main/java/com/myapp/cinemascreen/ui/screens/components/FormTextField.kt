package com.myapp.cinemascreen.ui.screens.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.cinemascreen.R
import com.myapp.cinemascreen.fontFamily
import com.myapp.cinemascreen.ui.theme.BlackText2
import com.myapp.cinemascreen.ui.theme.LightGrey

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorIndicator: Boolean,
    onErrorChange: (Boolean) -> Unit,
    validatorFunction: (String) -> Boolean,
    placeHolderText: String,
    errorText: String,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    //Email Input
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = { Text(text = placeHolderText) },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = LightGrey,
            unfocusedContainerColor = LightGrey,
            errorContainerColor = LightGrey
        ),
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = BlackText2
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (validatorFunction(value)) {
                    focusManager.moveFocus(FocusDirection.Next)
                } else {
                    onErrorChange(true)
                }
            }
        ),
        isError = errorIndicator,
        modifier = modifier
    )
    if (errorIndicator) {
        Text(
            text = errorText,
            fontFamily = fontFamily,
            fontSize = 12.sp,
            color = Color.Red
        )
    }
}

@Composable
fun FormPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorIndicator: Boolean,
    onErrorChange: (Boolean) -> Unit,
    validatorFunction: (String) -> Boolean,
    placeHolderText: String,
    errorText: String,
    focusManager: FocusManager,
    isInTheEnd: Boolean,
    modifier: Modifier = Modifier
){
    var isPasswordShow by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeHolderText) },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = LightGrey,
            unfocusedContainerColor = LightGrey,
            errorContainerColor = LightGrey
        ),
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = BlackText2
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = if(isInTheEnd) ImeAction.Done else ImeAction.Next
        ),
        visualTransformation = if (isPasswordShow) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardActions = KeyboardActions(
            onNext = {
                if (validatorFunction(value)) {
                    focusManager.moveFocus(FocusDirection.Next)
                }else{
                    onErrorChange(true)
                }
            },
            onDone = {
                if (validatorFunction(value)) {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                }else{
                    onErrorChange(true)
                }
            }
        ),
        trailingIcon = {
            IconButton(onClick = { isPasswordShow = !isPasswordShow }) {
                if (isPasswordShow) {
                    Icon(
                        painter = painterResource(id = R.drawable.mdi_eye_outline),
                        contentDescription = "show password"
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.mdi_hide_outline),
                        contentDescription = "show password"
                    )
                }
            }
        },
        isError = errorIndicator,
        modifier = modifier
    )
    if (errorIndicator) {
        Text(
            text = errorText,
            fontFamily = fontFamily,
            fontSize = 12.sp,
            color = Color.Red
        )
    }
}
