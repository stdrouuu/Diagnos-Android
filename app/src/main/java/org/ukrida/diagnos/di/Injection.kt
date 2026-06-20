package org.ukrida.diagnos.di

import org.ukrida.diagnos.data.api.ApiService
import org.ukrida.diagnos.data.api.RetrofitInstance
import org.ukrida.diagnos.data.repository.UserRepository

object Injection {
    private val api: ApiService = RetrofitInstance.api

    val userRepo: UserRepository by lazy {
        UserRepository(api)
    }
}
