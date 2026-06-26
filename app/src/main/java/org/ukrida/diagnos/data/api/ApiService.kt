// Interface Retrofit yang mendefinisikan seluruh endpoint API (GET, POST, dll)
package org.ukrida.diagnos.data.api

import org.ukrida.diagnos.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET(value = "users.php")
    suspend fun getUsers(): List<User>

    @POST(value = "users.php")
    suspend fun insertUser(
        @Body user: User
    ): Response<Unit>

    @POST(value = "update_user.php")
    suspend fun updateUser(
        @Body user: User
    ): Response<Unit>

    @GET(value = "delete_user.php")
    suspend fun deleteUser(
        @Query("id") id: Int
    ): Response<Unit>

    // ============= LOGIN =============
    @POST(value = "login.php")
    suspend fun login(
        @Body request: Map<String, String>
    ): User
}
