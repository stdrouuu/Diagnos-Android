package org.ukrida.diagnos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.ukrida.diagnos.data.model.AdminBooking
import java.util.Calendar
import java.util.Date

class AdminViewModel : ViewModel() {

    // Mock initial data
    private val _bookings = mutableStateOf(
        listOf(
            AdminBooking(
                id = "DG-9912",
                patientName = "Rina Kusuma",
                email = "rina.k@gmail.com",
                phone = "0812-3456-7890",
                testName = "Hematologi Lengkap",
                date = "20 Jul 2026",
                time = "08:00",
                status = "Dikonfirmasi",
                resultStatus = "Menunggu Hasil",
                gender = "Perempuan",
                dob = "14 Maret 1993",
                address = "Jl. Melati No. 12, Jakarta Selatan"
            ),
            AdminBooking(
                id = "DG-9913",
                patientName = "Budi Santoso",
                email = "budi.s@outlook.com",
                phone = "0821-9876-5432",
                testName = "Cek Darah Rutin & Nilai-Nilai MC",
                date = "20 Jul 2026",
                time = "09:30",
                status = "Selesai",
                resultStatus = "Hasil Siap",
                gender = "Laki-laki",
                dob = "22 Agustus 1985",
                address = "Jl. Sudirman Kav. 21, Jakarta Pusat"
            ),
            AdminBooking(
                id = "DG-9914",
                patientName = "Siti Rahayu",
                email = "siti.rahayu@yahoo.com",
                phone = "0813-1111-2222",
                testName = "Cek Darah Rutin & Nilai-Nilai MC",
                date = "21 Jul 2026",
                time = "10:00",
                status = "Dikonfirmasi",
                resultStatus = "Menunggu Hasil",
                gender = "Perempuan",
                dob = "05 Desember 1990",
                address = "Gg. Kelinci No. 8, Bandung"
            ),
            AdminBooking(
                id = "DG-9915",
                patientName = "Agus Wijaya",
                email = "agus@email.com",
                phone = "0815-4567-8901",
                testName = "Hitung Jenis Leukosit",
                date = "22 Jul 2026",
                time = "08:30",
                status = "Dikonfirmasi",
                resultStatus = "Menunggu Hasil",
                gender = "Laki-laki",
                dob = "17 Juli 1978",
                address = "Jl. Merdeka No. 102, Surabaya"
            ),
            AdminBooking(
                id = "DG-9916",
                patientName = "Dewi Lestari",
                email = "dewi.les@gmail.com",
                phone = "0857-3333-4444",
                testName = "Hematologi Lengkap",
                date = "22 Jul 2026",
                time = "11:00",
                status = "Menunggu",
                resultStatus = "Menunggu Hasil",
                gender = "Perempuan",
                dob = "29 Oktober 1997",
                address = "Perum Gading Indah Blok C/4, Jakarta Utara"
            )
        )
    )
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
    val totalResultQueue: Int get() = _bookings.value.count { it.status == "Dikonfirmasi" && it.resultStatus == "Menunggu Hasil" }
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

        _bookings.value = _bookings.value.map { item ->
            if (item.id == booking.id) {
                val updatedResultStatus = if (newStatus == "Selesai") "Hasil Siap" else item.resultStatus
                item.copy(status = newStatus, resultStatus = updatedResultStatus)
            } else {
                item
            }
        }

        showToast("Status pesanan ${booking.patientName} disimpan menjadi $newStatus.")
        selectBookingForDetail(null)
    }

    fun saveInputResults(bookingId: String) {
        _bookings.value = _bookings.value.map { item ->
            if (item.id == bookingId) {
                item.copy(status = "Selesai", resultStatus = "Hasil Siap")
            } else {
                item
            }
        }
        showToast("Data lab berhasil disimpan dan dirilis ke akun Pasien.")
        selectedBookingForInput.value = null
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
