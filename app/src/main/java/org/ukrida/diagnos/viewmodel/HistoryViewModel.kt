package org.ukrida.diagnos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.ukrida.diagnos.data.model.TestHistoryItem

class HistoryViewModel : ViewModel() {
    private val _historyList = mutableStateOf<List<TestHistoryItem>>(
        listOf(
            TestHistoryItem(
                id = 1,
                testId = 1,
                title = "Hematologi (Lengkap)",
                date = "15 Okt 2025",
                clinicName = "Klinik Cinta Kasih PIK",
                status = "Selesai"
            ),
            TestHistoryItem(
                id = 2,
                testId = 2,
                title = "Hitung Jenis Leukosit",
                date = "02 Sep 2025",
                clinicName = "Klinik Cinta Kasih PIK",
                status = "Selesai"
            )
        )
    )
    val historyList: State<List<TestHistoryItem>> = _historyList

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun syncWithBooking(bookingViewModel: BookingViewModel) {
        if (bookingViewModel.isOrderCompleted) {
            val test = bookingViewModel.selectedTest ?: return
            val exists = _historyList.value.any { it.id == test.id + 100 }
            if (!exists) {
                val newHistory = TestHistoryItem(
                    id = test.id + 100,
                    testId = test.id,
                    title = test.title,
                    date = bookingViewModel.selectedDate,
                    clinicName = bookingViewModel.selectedClinic,
                    status = "Diproses"
                )
                // Add to the front of the list
                _historyList.value = listOf(newHistory) + _historyList.value
            }
        }
    }
}
