package com.example.testapp

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.kotlin.core.Amplify

class AmplifyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            Amplify.addPlugin(AWSDataStorePlugin())
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
        } catch (error: AmplifyException) {
            Log.e("App", "Could not init Amplify", error)
        }
    }
}