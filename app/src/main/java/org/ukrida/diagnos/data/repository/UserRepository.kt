package org.ukrida.diagnos.data.repository

import org.ukrida.diagnos.data.api.ApiService
import org.ukrida.diagnos.data.model.User

class UserRepository(private val api: ApiService) {
    suspend fun getUsers() = api.getUsers()

    suspend fun insert(user: User) = api.insertUser(user)

    suspend fun update(user: User) = api.updateUser(user)

    suspend fun delete(id: Int) = api.deleteUser(id)

    suspend fun login(username: String, password: String): User {
        return api.login(request = mapOf(
            "username" to username,
            "password" to password
        ))
    }
}
