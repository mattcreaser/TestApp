package com.example.testapp.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimens(
    val medium: Dp = 16.dp
)

val LocalDimens = staticCompositionLocalOf { Dimens() }