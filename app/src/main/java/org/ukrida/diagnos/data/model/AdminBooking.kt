package org.ukrida.diagnos.data.model

data class AdminBooking(
    val id: String,
    val patientName: String,
    val email: String,
    val phone: String,
    val testName: String,
    val date: String,
    val time: String,
    var status: String, // "Menunggu", "Dikonfirmasi", "Sedang diuji", "Selesai", "Dibatalkan"
    var resultStatus: String, // "Menunggu Hasil", "Hasil Siap"
    val gender: String,
    val dob: String,
    val address: String,
    val clinicName: String
)

