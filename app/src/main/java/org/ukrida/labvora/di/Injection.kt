package org.ukrida.labvora.di

import org.ukrida.labvora.data.api.ApiService
import org.ukrida.labvora.data.api.RetrofitInstance
import org.ukrida.labvora.data.repository.UserRepository

object Injection {
    private val api: ApiService = RetrofitInstance.api

    val userRepo: UserRepository by lazy {
        UserRepository(api)
    }
}
