package com.viltrumites.khuluma.data

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseNetworkException
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
    private var idTokenListener: FirebaseAuth.IdTokenListener? = null

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
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()

            Log.i("AuthManager", "Firebase sign-in ok: ${result.user?.uid}")
            result.user?.displayName ?: "Learner"
        } catch (e: FirebaseNetworkException) {
            val cachedUser = auth.currentUser

            if (cachedUser != null) {
                Log.w("AuthManager", "Network unavailable; continuing with cached Firebase session")
                cachedUser.displayName ?: "Learner"
            } else {
                Log.w("AuthManager", "Sign-in unavailable while offline", e)
                throw IllegalStateException(
                    "No internet connection. Check your connection and try again.",
                    e
                )
            }
        }
    }

    /**
     * Watches Firebase for ID-token changes so callers can react when a token
     * is refreshed or the authenticated user changes.
     */
    fun startTokenRefreshListener(onTokenChanged: (String?) -> Unit) {
        stopTokenRefreshListener()

        val listener = FirebaseAuth.IdTokenListener { firebaseAuth ->
            val user = firebaseAuth.currentUser

            if (user == null) {
                onTokenChanged(null)
            } else {
                user.getIdToken(false)
                    .addOnSuccessListener { result ->
                        onTokenChanged(result.token)
                    }
                    .addOnFailureListener { error ->
                        Log.w(
                            "AuthManager",
                            "Unable to refresh Firebase ID token: ${error.message}"
                        )
                        onTokenChanged(null)
                    }
            }
        }

        idTokenListener = listener
        auth.addIdTokenListener(listener)
    }

    fun stopTokenRefreshListener() {
        idTokenListener?.let { auth.removeIdTokenListener(it) }
        idTokenListener = null
    }

    suspend fun signOut() {
        stopTokenRefreshListener()

        try {
            googleClient().signOut().await()
        } catch (e: Exception) {
            Log.w("AuthManager", "Google sign-out unavailable: ${e.message}")
        }

        auth.signOut()
        Log.i("AuthManager", "Signed out")
    }
}