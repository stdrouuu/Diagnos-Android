// Interface Retrofit yang mendefinisikan seluruh endpoint API (GET, POST, dll)
package org.ukrida.diagnos.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ukrida.diagnos.data.model.AdminBooking
import org.ukrida.diagnos.data.model.TestHistoryItem
import org.ukrida.diagnos.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Multipart
import retrofit2.http.Part

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

    // Login
    @POST(value = "login.php")
    suspend fun login(
        @Body request: Map<String, String>
    ): User

    // Bookings
    @GET(value = "bookings.php")
    suspend fun getBookings(): List<AdminBooking>

    @GET(value = "bookings.php")
    suspend fun getUserBookings(
        @Query("user_id") userId: Int
    ): List<TestHistoryItem>

    @Multipart
    @POST(value = "bookings.php")
    suspend fun createBookingMultipart(
        @Part("user_id") userId: RequestBody,
        @Part("test_id") testId: RequestBody,
        @Part("booking_date") bookingDate: RequestBody,
        @Part("booking_time") bookingTime: RequestBody,
        @Part("clinic_name") clinicName: RequestBody,
        @Part referralPhoto: MultipartBody.Part?
    ): Response<Unit>

    @POST(value = "bookings.php")
    suspend fun createBooking(
        @Body request: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Unit>

    @POST(value = "bookings.php?action=update_status")
    suspend fun updateBookingStatus(
        @Body request: Map<String, @JvmSuppressWildcards Any>
    ): Response<Unit>

    // Results
    @POST(value = "results.php")
    suspend fun saveLabResults(
        @Body results: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Unit>

    @GET(value = "results.php")
    suspend fun getLabResults(
        @Query("booking_id") bookingId: Int
    ): Map<String, String?>
}
