package org.ukrida.diagnos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.ukrida.diagnos.data.model.TestHistoryItem

class HistoryViewModel : ViewModel() {

    // Only COMPLETED ("Selesai") orders appear here — populated by backend later
    private val _historyList = mutableStateOf<List<TestHistoryItem>>(
        listOf(
            TestHistoryItem(
                id = 1,
                testId = 1,
                title = "Hematologi (Lengkap)",
                date = "15 Mei 2026",
                clinicName = "Klinik Cinta Kasih PIK",
                status = "Selesai"
            ),
            TestHistoryItem(
                id = 2,
                testId = 2,
                title = "Hitung Jenis Leukosit",
                date = "02 Apr 2026",
                clinicName = "Klinik Cinta Kasih PIK",
                status = "Selesai"
            ),
            TestHistoryItem(
                id = 3,
                testId = 3,
                title = "Cek Darah Rutin & Nilai-Nilai MC",
                date = "18 Feb 2026",
                clinicName = "Klinik Cinta Kasih PIK",
                status = "Selesai"
            )
        )
    )
    val historyList: State<List<TestHistoryItem>> = _historyList

    // Active pending order — status "Menunggu" or in-progress, NOT yet in history
    private val _pendingOrder = mutableStateOf<TestHistoryItem?>(null)
    val pendingOrder: State<TestHistoryItem?> = _pendingOrder

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // Called when user confirms order — sets pendingOrder only, does NOT add to historyList
    fun syncWithBooking(bookingViewModel: BookingViewModel) {
        if (bookingViewModel.isOrderCompleted) {
            val test = bookingViewModel.selectedTest ?: return
            if (_pendingOrder.value == null) {
                _pendingOrder.value = TestHistoryItem(
                    id = test.id + 100,
                    testId = test.id,
                    title = test.title,
                    date = bookingViewModel.selectedDate,
                    clinicName = bookingViewModel.selectedClinic,
                    status = "Menunggu"
                )
            }
        }
    }
}

