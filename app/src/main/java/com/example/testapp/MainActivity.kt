package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.amplifyframework.kotlin.core.Amplify
import com.example.testapp.extensions.Authenticated
import com.example.testapp.extensions.Unauthenticated
import com.example.testapp.extensions.loginStatus
import com.example.testapp.ui.login.LoginScreen
import com.example.testapp.ui.main.MainScreen
import com.example.testapp.ui.theme.TestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authStatus by Amplify.Auth.loginStatus().collectAsState(initial = Unauthenticated())

            TestAppTheme {
                when (val status = authStatus) {
                    is Authenticated -> MainScreen()
                    is Unauthenticated -> LoginScreen(status.reason)
                }
            }
        }
    }
}