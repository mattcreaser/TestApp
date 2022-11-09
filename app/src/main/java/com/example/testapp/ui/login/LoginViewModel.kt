package com.example.testapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _screenState = MutableStateFlow<LoginScreenState>(EnteringLogin("", ""))
    val screenState = _screenState.asStateFlow()

    fun onEnterLoginInfo(email: String, password: String) {
        _screenState.value = EnteringLogin(email, password)
    }

    fun onEnterRegistrationInfo(email: String, password: String) {
        _screenState.value = EnteringRegistration(email, password)
    }

    fun onEnterConfirmationCode(code: String) {
        val state = _screenState.value
        if (state is EnteringConfirmation) {
            _screenState.value = EnteringConfirmation(state.email, code)
        }
    }

    fun onSubmit() {
        val state = _screenState.value
        _screenState.value = Loading
        when (state) {
            is EnteringConfirmation -> submitRegistrationConfirmation(state.email, state.code)
            is EnteringLogin -> submitLogin(state.email, state.password)
            is EnteringRegistration -> submitRegistration(state.email, state.password)
            else -> Unit
        }
    }

    fun onToggleRegister() {
        when (val state = _screenState.value) {
            is EnteringLogin -> _screenState.value =
                EnteringRegistration(state.email, state.password)
            is EnteringRegistration -> _screenState.value =
                EnteringLogin(state.email, state.password)
            else -> Unit
        }
    }

    private fun submitLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = Amplify.Auth.signIn(username = email, password = password)
                if (!result.isSignedIn) {
                    _screenState.value = EnteringLogin(email, password, "Not signed in")
                } else {
                    _screenState.value = EnteringLogin("", "")
                }
            } catch (error: AuthException) {
                Log.e("App", "Sign in failed", error)
                _screenState.value = EnteringLogin(email, password, error.message)
            }
        }
    }

    private fun submitRegistration(email: String, password: String) {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        viewModelScope.launch {
            try {
                val result = Amplify.Auth.signUp(email, password, options)
                if (!result.isSignUpComplete) {
                    _screenState.value = EnteringConfirmation(email, "")
                }
                Log.i("AuthQuickStart", "Result: $result")
            } catch (error: AuthException) {
                Log.e("AuthQuickStart", "Sign up failed", error)
                _screenState.value = EnteringRegistration(email, password, error.message)
            }
        }
    }

    private fun submitRegistrationConfirmation(email: String, code: String) {
        viewModelScope.launch {
            try {
                val result = Amplify.Auth.confirmSignUp(email, code)
                if (result.isSignUpComplete) {
                    _screenState.value = EnteringLogin(email, "")
                }
            } catch (error: AuthException) {
                Log.e("App", "Failed to confirm signup", error)
            }
        }
    }
}