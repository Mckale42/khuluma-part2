package com.viltrumites.khuluma

import android.content.Context
import com.viltrumites.khuluma.data.AuthManager
import com.viltrumites.khuluma.data.remote.ApiClient
import com.viltrumites.khuluma.data.repo.KhulumaRepository

/**
 * Lightweight manual dependency container. Keeps the prototype simple (no Hilt),
 * while still giving every screen the same repository + auth instances.
 */
object ServiceLocator {
    lateinit var repository: KhulumaRepository
        private set
    lateinit var auth: AuthManager
        private set

    fun init(context: Context) {
        auth = AuthManager(context.applicationContext)
        repository = KhulumaRepository(ApiClient.create())
    }
}
