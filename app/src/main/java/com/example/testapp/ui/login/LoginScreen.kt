package com.example.testapp.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapp.R
import com.example.testapp.extensions.UnauthenticatedReason
import com.example.testapp.ui.theme.AppTheme

@Composable
fun LoginScreen(reason: UnauthenticatedReason) {
    val viewModel = viewModel<LoginViewModel>()
    val state by viewModel.screenState.collectAsState()
    LoginScreen(
        reason = reason,
        state = state,
        onEnterConfirmationCode = viewModel::onEnterConfirmationCode,
        onEnterLoginInfo = viewModel::onEnterLoginInfo,
        onEnterRegistrationInfo = viewModel::onEnterRegistrationInfo,
        onSubmit = viewModel::onSubmit,
        onToggleRegister = viewModel::onToggleRegister
    )
}

@Composable
fun LoginScreen(
    reason: UnauthenticatedReason,
    state: LoginScreenState,
    onEnterConfirmationCode: (String) -> Unit,
    onEnterLoginInfo: (String, String) -> Unit,
    onEnterRegistrationInfo: (String, String) -> Unit,
    onSubmit: () -> Unit,
    onToggleRegister: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state == Loading) {
                LoginLoading()
            } else {
                Card {
                    when (state) {
                        Loading -> Unit
                        is EnteringConfirmation -> ConfirmationCode(
                            state = state,
                            onDataChange = onEnterConfirmationCode,
                            onSubmit = onSubmit
                        )
                        is EnteringLogin -> LoginInput(
                            state = state,
                            onDataChange = onEnterLoginInfo,
                            onSubmit = onSubmit,
                            onRegister = onToggleRegister
                        )
                        is EnteringRegistration -> RegisterInput(
                            state = state,
                            onDataChange = onEnterRegistrationInfo,
                            onSubmit = onSubmit,
                            onLogin = onToggleRegister
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(reason) {
        if (reason != UnauthenticatedReason.None) {
            scaffoldState.snackbarHostState.showSnackbar(reason.name)
        }
    }
}

@Composable
fun LoginLoading() {
    CircularProgressIndicator()
}

@Composable
fun LoginInput(
    state: EnteringLogin,
    onDataChange: (String, String) -> Unit,
    onSubmit: () -> Unit,
    onRegister: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(AppTheme.dimens.medium)
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = { onDataChange(it, state.password) },
            singleLine = true,
            label = { Text(text = stringResource(R.string.register_label_email)) }
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { onDataChange(state.email, it) },
            singleLine = true,
            label = { Text(text = stringResource(R.string.register_label_password)) },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = onSubmit) {
            Text(text = stringResource(R.string.login_button_login))
        }
        state.error?.let {
            ErrorMessage(message = it)
        }
        TextButton(onClick = onRegister) {
            Text(text = stringResource(R.string.login_button_register))
        }
    }
}

@Composable
fun RegisterInput(
    state: EnteringRegistration,
    onDataChange: (String, String) -> Unit,
    onSubmit: () -> Unit,
    onLogin: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(AppTheme.dimens.medium)
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = { onDataChange(it, state.password) },
            singleLine = true,
            label = { Text(text = stringResource(R.string.register_label_email)) }
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { onDataChange(state.email, it) },
            singleLine = true,
            label = { Text(text = stringResource(R.string.register_label_password)) },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = onSubmit) {
            Text(text = stringResource(R.string.login_button_register))
        }
        state.error?.let {
            ErrorMessage(message = it)
        }
        TextButton(onClick = onLogin) {
            Text(text = stringResource(R.string.login_button_login))
        }
    }
}

@Composable
fun ConfirmationCode(
    state: EnteringConfirmation,
    onDataChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(AppTheme.dimens.medium)
    ) {
        Text(text = stringResource(R.string.login_confirm_message, state.email))
        OutlinedTextField(
            value = state.code,
            onValueChange = onDataChange,
            singleLine = true,
            label = {
                Text(text = stringResource(R.string.login_label_code))
            }
        )
        Button(onClick = onSubmit) {
            Text(text = stringResource(R.string.button_submit))
        }
        state.error?.let {
            ErrorMessage(message = it)
        }
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(text = message, color = AppTheme.colors.error)
}

sealed interface LoginScreenState

object Loading : LoginScreenState

data class EnteringLogin(
    val email: String,
    val password: String,
    val error: String? = null
) : LoginScreenState

data class EnteringRegistration(
    val email: String,
    val password: String,
    val error: String? = null
) : LoginScreenState

data class EnteringConfirmation(
    val email: String,
    val code: String,
    val error: String? = null
) : LoginScreenState