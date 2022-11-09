package com.example.testapp.extensions

import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.auth.KotlinAuthFacade
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

enum class UnauthenticatedReason {
    None,
    SignedOut,
    SessionExpired,
    UserDeleted
}

sealed interface AuthStatus
class Authenticated(val session: AuthSession) : AuthStatus
class Unauthenticated(val reason: UnauthenticatedReason = UnauthenticatedReason.None) : AuthStatus

fun KotlinAuthFacade.loginStatus() = flow {
    try {
        val currentSession = Amplify.Auth.fetchAuthSession()
        if (currentSession.isSignedIn) {
            emit(Authenticated(currentSession))
        }
    } catch (error: AuthException) {
        emit(Unauthenticated())
    }

    Amplify.Hub.subscribe(HubChannel.AUTH)
        .filter { it.name != InitializationStatus.SUCCEEDED.toString() && it.name != InitializationStatus.FAILED.toString() }
        .map { enumValueOf<AuthChannelEventName>(it.name) }
        .collect { event ->
            when (event) {
                AuthChannelEventName.SIGNED_OUT -> emit(Unauthenticated(UnauthenticatedReason.SignedOut))
                AuthChannelEventName.SIGNED_IN -> {
                    val currentSession = Amplify.Auth.fetchAuthSession()
                    if (currentSession.isSignedIn) {
                        emit(Authenticated(currentSession))
                    }
                }
                AuthChannelEventName.SESSION_EXPIRED -> emit(Unauthenticated(UnauthenticatedReason.SessionExpired))
                AuthChannelEventName.USER_DELETED -> emit(Unauthenticated(UnauthenticatedReason.UserDeleted))
            }
        }
}