package org.ukrida.diagnos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ukrida.diagnos.data.model.AdminBooking
import java.util.Calendar
import java.util.Date

class AdminViewModel : ViewModel() {

    // Mock initial data
    private val _bookings = mutableStateOf<List<AdminBooking>>(emptyList())

    init {
        getBookings()
    }

    fun getBookings() {
        viewModelScope.launch {
            try {
                _bookings.value = org.ukrida.diagnos.data.api.RetrofitInstance.api.getBookings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    val bookings: State<List<AdminBooking>> = _bookings

    // Filters and search states
    val searchQuery = mutableStateOf("")
    val searchDate = mutableStateOf("") // YYYY-MM-DD format from DatePickerDialog
    val activeStatusFilter = mutableStateOf("Semua")

    // Chart States
    val chartTimeframe = mutableStateOf("MingguIni") // "MingguIni" or "MingguLalu"
    val selectedChartIndex = mutableStateOf(3)
    val chartDataSets = mapOf(
        "MingguIni" to listOf(4, 8, 5, 12, 9, 15, 10),
        "MingguLalu" to listOf(6, 4, 11, 7, 14, 8, 12)
    )
    val chartLabels = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")

    // Detail Bottom Sheet States
    val selectedBookingForDetail = mutableStateOf<AdminBooking?>(null)
    val selectedStatusValue = mutableStateOf("Menunggu")

    // Input Results States
    val selectedBookingForInput = mutableStateOf<AdminBooking?>(null)

    // Feedback States
    val toastMessage = mutableStateOf<String?>(null)
    val toastIsError = mutableStateOf(false)

    // Derived Statistics
    val totalBookings: Int get() = _bookings.value.size
    val totalPending: Int get() = _bookings.value.count { it.status == "Menunggu" }
    val totalResultQueue: Int get() = _bookings.value.count { it.status == "Sedang diuji" && it.resultStatus == "Menunggu Hasil" }
    val totalTesting: Int get() = _bookings.value.count { it.status == "Sedang diuji" }
    val totalSuccess: Int get() = _bookings.value.count { it.status == "Selesai" }

    // Actions
    fun filterBookings(): List<AdminBooking> {
        val query = searchQuery.value.lowercase().trim()
        val dateFilter = searchDate.value // Format YYYY-MM-DD
        val filterTab = activeStatusFilter.value

        return _bookings.value.filter { booking ->
            // Filter status tab
            if (filterTab != "Semua" && booking.status != filterTab) return@filter false

            // Filter search text
            if (query.isNotEmpty() &&
                !booking.patientName.lowercase().contains(query) &&
                !booking.id.lowercase().contains(query)
            ) return@filter false

            // Filter date picker format e.g. "20 Jun 2026" vs "2026-06-20" or similar
            if (dateFilter.isNotEmpty()) {
                val formattedBookingDate = convertBookingDateToISO(booking.date)
                if (formattedBookingDate != dateFilter) return@filter false
            }

            true
        }
    }

    private fun convertBookingDateToISO(dateStr: String): String {
        // Format: "20 Jun 2026"
        val parts = dateStr.split(" ")
        if (parts.size == 3) {
            val day = parts[0].padStart(2, '0')
            val monthStr = parts[1]
            val year = parts[2]

            val months = mapOf(
                "Jan" to "01", "Feb" to "02", "Mar" to "03", "Apr" to "04",
                "Mei" to "05", "Jun" to "06", "Jul" to "07", "Agu" to "08",
                "Sep" to "09", "Okt" to "10", "Nov" to "11", "Des" to "12"
            )
            val month = months[monthStr] ?: "01"
            return "$year-$month-$day"
        }
        return ""
    }

    fun selectBookingForDetail(booking: AdminBooking?) {
        selectedBookingForDetail.value = booking
        if (booking != null) {
            selectedStatusValue.value = booking.status
        }
    }

    fun updateSelectedStatusValue(status: String) {
        val originalStatus = selectedBookingForDetail.value?.status ?: return

        // Safeguard policies
        if (originalStatus == "Menunggu" && (status == "Sedang diuji" || status == "Selesai")) {
            showToast("Tindakan tidak valid! Dari status Menunggu, Anda hanya bisa mengubah ke Dikonfirmasi atau Dibatalkan.", true)
            return
        }

        if ((originalStatus == "Dikonfirmasi" || originalStatus == "Sedang diuji" || originalStatus == "Selesai") && status == "Dibatalkan") {
            showToast("Pembatalan diblokir oleh sistem! Pesanan terkonfirmasi tidak dapat dibatalkan.", true)
            return
        }

        if ((originalStatus == "Dikonfirmasi" || originalStatus == "Sedang diuji" || originalStatus == "Selesai" || originalStatus == "Dibatalkan") && status == "Menunggu") {
            showToast("Tindakan tidak valid! Tidak dapat memindahkan pesanan kembali ke status Menunggu.", true)
            return
        }

        if ((originalStatus == "Sedang diuji" || originalStatus == "Selesai") && status == "Dikonfirmasi") {
            showToast("Tindakan tidak valid! Pesanan sedang diuji atau selesai tidak dapat kembali ke Dikonfirmasi.", true)
            return
        }

        selectedStatusValue.value = status
    }

    fun saveStatusChange() {
        val booking = selectedBookingForDetail.value ?: return
        val newStatus = selectedStatusValue.value

        viewModelScope.launch {
            try {
                val response = org.ukrida.diagnos.data.api.RetrofitInstance.api.updateBookingStatus(
                    mapOf(
                        "id" to booking.id.toInt(),
                        "status" to newStatus
                    )
                )
                if (response.isSuccessful) {
                    getBookings()
                    showToast("Status pesanan ${booking.patientName} disimpan menjadi $newStatus.")
                } else {
                    showToast("Gagal merubah status: ${response.message()}", isError = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Terjadi kesalahan koneksi.", isError = true)
            } finally {
                selectBookingForDetail(null)
            }
        }
    }

    fun saveInputResults(bookingId: String, resultsMap: Map<String, String>) {
        viewModelScope.launch {
            try {
                val payload = mutableMapOf<String, Any?>()
                payload["booking_id"] = bookingId.toInt()
                resultsMap.forEach { (key, value) ->
                    val dbKey = key.lowercase().replace("-", "_")
                    payload[dbKey] = value
                }

                val response = org.ukrida.diagnos.data.api.RetrofitInstance.api.saveLabResults(payload)
                if (response.isSuccessful) {
                    getBookings()
                    showToast("Data lab berhasil disimpan dan dirilis ke akun Pasien.")
                } else {
                    showToast("Gagal menyimpan hasil lab: ${response.message()}", isError = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Terjadi kesalahan koneksi.", isError = true)
            } finally {
                selectedBookingForInput.value = null
            }
        }
    }

    fun getUpcomingBooking(): AdminBooking? {
        val todayMs = System.currentTimeMillis()
        return _bookings.value
            .filter { it.status == "Dikonfirmasi" || it.status == "Menunggu" }
            .mapNotNull { booking ->
                val date = parseBookingDate(booking.date) ?: return@mapNotNull null
                booking to date
            }
            .sortedWith(compareBy<Pair<AdminBooking, Date>> { (_, date) ->
                if (date.time >= todayMs) 0 else 1
            }.thenBy { (_, date) ->
                Math.abs(date.time - todayMs)
            })
            .firstOrNull()?.first
    }

    private fun parseBookingDate(dateStr: String): Date? {
        val parts = dateStr.split(" ")
        if (parts.size == 3) {
            try {
                val day = parts[0].toInt()
                val monthStr = parts[1]
                val year = parts[2].toInt()

                val months = mapOf(
                    "Jan" to Calendar.JANUARY, "Feb" to Calendar.FEBRUARY, "Mar" to Calendar.MARCH,
                    "Apr" to Calendar.APRIL, "Mei" to Calendar.MAY, "Jun" to Calendar.JUNE,
                    "Jul" to Calendar.JULY, "Agu" to Calendar.AUGUST, "Sep" to Calendar.SEPTEMBER,
                    "Okt" to Calendar.OCTOBER, "Nov" to Calendar.NOVEMBER, "Des" to Calendar.DECEMBER
                )
                val month = months[monthStr] ?: Calendar.JANUARY

                val cal = Calendar.getInstance()
                cal.set(year, month, day, 0, 0, 0)
                cal.set(Calendar.MILLISECOND, 0)
                return cal.time
            } catch (e: Exception) {
                return null
            }
        }
        return null
    }

    fun showToast(message: String, isError: Boolean = false) {
        toastMessage.value = message
        toastIsError.value = isError
    }

    fun clearToast() {
        toastMessage.value = null
    }
}
