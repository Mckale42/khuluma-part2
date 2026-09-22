package com.viltrumites.khuluma.data.remote

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Attaches the signed-in user's Firebase ID token to every request
 * ("Authorization: Bearer <token>"), so the API can verify identity (NR-4).
 * Runs on OkHttp's background thread, so blocking on the token task is safe.
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = try {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) Tasks.await(user.getIdToken(false)).token else null
        } catch (e: Exception) {
            Log.w("AuthInterceptor", "Could not fetch ID token: ${e.message}")
            null
        }
        val request = if (token != null) {
            original.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else {
            // No Firebase session (e.g. demo mode): send a dev identity the API
            // honours only when it is running with auth bypass / demo mode on.
            original.newBuilder().addHeader("x-dev-user", "demo1").build()
        }
        return chain.proceed(request)
    }
}
