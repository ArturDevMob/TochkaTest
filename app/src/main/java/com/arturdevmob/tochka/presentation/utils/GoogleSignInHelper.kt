package com.arturdevmob.tochka.presentation.utils

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient

class GoogleSignInHelper(private val context: Context) {
    private val googleSignInOptions: GoogleSignInOptions
    private val googleApiClient: GoogleApiClient
    private val googleSignInClient: GoogleSignInClient

    init {
        googleSignInOptions = createGoogleSignInOptions()
        googleApiClient = createGoogleApiClient(googleSignInOptions)
        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    }

    fun disconnect() {
        googleApiClient.disconnect()
    }

    fun connect() {
        googleApiClient.connect()
    }

    fun getIntent(): Intent {
        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
    }

    fun signOutAccountGoogle() {
        googleSignInClient.revokeAccess()
    }

    private fun createGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    private fun createGoogleApiClient(options: GoogleSignInOptions): GoogleApiClient {
        return GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, options)
            .build()
    }
}