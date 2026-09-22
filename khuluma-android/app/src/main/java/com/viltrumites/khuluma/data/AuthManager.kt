package com.viltrumites.khuluma.data

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.viltrumites.khuluma.R
import kotlinx.coroutines.tasks.await

/**
 * Single-sign-on manager (FR-1). Wraps Google Sign-In + Firebase Authentication:
 * the app gets a Google ID token, exchanges it for a Firebase session, and the
 * API then verifies that Firebase token on every request.
 */
class AuthManager(private val appContext: Context) {

    private val auth = FirebaseAuth.getInstance()

    val isSignedIn: Boolean get() = auth.currentUser != null
    val displayName: String get() = auth.currentUser?.displayName ?: "Learner"

    fun googleClient(): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(appContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(appContext, options)
    }

    fun signInIntent(): Intent = googleClient().signInIntent

    /** Completes sign-in from the Google intent result; returns the display name. */
    suspend fun firebaseSignInWithGoogleToken(idToken: String): String {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        Log.i("AuthManager", "Firebase sign-in ok: ${result.user?.uid}")
        return result.user?.displayName ?: "Learner"
    }

    suspend fun signOut() {
        try { googleClient().signOut().await() } catch (_: Exception) {}
        auth.signOut()
        Log.i("AuthManager", "Signed out")
    }
}
