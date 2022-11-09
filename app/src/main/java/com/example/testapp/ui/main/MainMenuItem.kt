package com.example.testapp.ui.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.testapp.R

enum class MainMenuItem(
    @StringRes val label: Int,
    val icon: ImageVector
) {
    PROFILE(R.string.menu_item_profile, Icons.Default.Person)
}