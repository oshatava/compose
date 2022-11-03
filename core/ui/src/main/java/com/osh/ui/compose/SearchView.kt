package com.osh.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchView(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    placeholderTextStyle: TextStyle = TextStyle.Default,
    textStyle: TextStyle = TextStyle.Default,
    color: Color = Color.White,
    bgColor: Color = Color.LightGray,
    icon: @Composable () -> Unit,
    modifier: Modifier,
) {
    var value by rememberSaveable { mutableStateOf(value) }
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    Box(modifier) {
        BasicTextField(
            value = value,
            onValueChange = {
                value = it
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus(true)
                }
            ),
            cursorBrush = SolidColor(color),
            singleLine = true,
            textStyle = textStyle,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .then(modifier)
                        .background(bgColor, RoundedCornerShape(CornerSize(10.dp)))
                        .padding(top = 7.dp, bottom = 7.dp, start = 4.dp, end = 4.dp)
                ) {
                    icon()
                    Spacer(Modifier.width(16.dp))
                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        innerTextField()
                        if (value.isEmpty() && placeholderText.isNotEmpty()) {
                            Text(
                                text = placeholderText,
                                style = placeholderTextStyle,
                            )
                        }
                    }
                }
            }
        )
    }
}