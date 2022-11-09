package com.example.testapp.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun onSignOut() {
        viewModelScope.launch {
            try {
                Amplify.Auth.signOut()
            } catch (error: AuthException) {
                Log.e("Main", "Sign out failed", error)
            }
        }
    }
}