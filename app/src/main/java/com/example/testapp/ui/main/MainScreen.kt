package com.example.testapp.ui.main

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapp.R
import com.example.testapp.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewModel = viewModel<MainViewModel>()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            MainMenu(onClickSignOut = viewModel::onSignOut)
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Menu, "")
                    }
                }
            )
        }
    ) {

    }
}

@Composable
fun ColumnScope.MainMenu(
    onClickSignOut: () -> Unit
) {
    MainMenuItem.values().forEach { MainMenuItem(it) }
    Divider()
    TextButton(onClick = onClickSignOut, modifier = Modifier.align(Alignment.CenterHorizontally)) {
        Text(text = stringResource(R.string.menu_item_signout))
    }
}

@Composable
fun MainMenuItem(item: MainMenuItem) {
    Row(modifier = Modifier.padding(AppTheme.dimens.medium)) {
        Icon(imageVector = item.icon, contentDescription = null)
        Text(text = stringResource(item.label))
    }
}