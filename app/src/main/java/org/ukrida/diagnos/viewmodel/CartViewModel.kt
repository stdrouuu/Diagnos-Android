package org.ukrida.diagnos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ukrida.diagnos.data.api.RetrofitInstance
import org.ukrida.diagnos.data.model.CartItem
import org.ukrida.diagnos.data.model.LabTest
import java.text.NumberFormat
import java.util.Locale

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val cartItems: State<List<CartItem>> = _cartItems

    val toastMessage = mutableStateOf<String?>(null)
    val showCheckoutSuccessModal = mutableStateOf(false)
    val isCheckingOut = mutableStateOf(false)

    val cartItemCount: Int
        get() = _cartItems.value.size

    val checkedCount: Int
        get() = _cartItems.value.count { it.isChecked }

    val isAllChecked: Boolean
        get() = _cartItems.value.isNotEmpty() && _cartItems.value.all { it.isChecked }

    val totalPriceVal: Int
        get() = _cartItems.value.filter { it.isChecked }.sumOf { it.test.priceVal }

    val totalPriceFormatted: String
        get() {
            val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            return formatter.format(totalPriceVal).replace(",00", "")
        }

    fun addToCart(
        test: LabTest,
        clinicName: String = "Klinik Citra Kasih PIK",
        bookingDate: String = "2026-6-11",
        bookingTime: String = "14:00",
        hasDoctorReferral: Boolean = false
    ) {
        val currentList = _cartItems.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.test.id == test.id }
        if (existingIndex != -1) {
            val existing = currentList[existingIndex]
            currentList[existingIndex] = existing.copy(
                clinicName = clinicName,
                bookingDate = bookingDate,
                bookingTime = bookingTime,
                hasDoctorReferral = hasDoctorReferral,
                isChecked = true
            )
        } else {
            currentList.add(
                CartItem(
                    test = test,
                    clinicName = clinicName,
                    bookingDate = bookingDate,
                    bookingTime = bookingTime,
                    hasDoctorReferral = hasDoctorReferral,
                    isChecked = true
                )
            )
        }
        _cartItems.value = currentList
        toastMessage.value = "${test.title} berhasil ditambahkan ke Keranjang!"
    }

    fun removeFromCart(cartItemId: String) {
        _cartItems.value = _cartItems.value.filter { it.id != cartItemId }
    }

    fun updateItemSchedule(cartItemId: String, clinicName: String, bookingDate: String, bookingTime: String) {
        _cartItems.value = _cartItems.value.map { item ->
            if (item.id == cartItemId) {
                item.copy(
                    clinicName = clinicName,
                    bookingDate = bookingDate,
                    bookingTime = bookingTime
                )
            } else {
                item
            }
        }
        toastMessage.value = "Jadwal dan lokasi berhasil diperbarui!"
    }

    fun toggleItemChecked(cartItemId: String) {
        _cartItems.value = _cartItems.value.map { item ->
            if (item.id == cartItemId) {
                item.copy(isChecked = !item.isChecked)
            } else {
                item
            }
        }
    }

    fun toggleSelectAll(checked: Boolean) {
        _cartItems.value = _cartItems.value.map { item ->
            item.copy(isChecked = checked)
        }
    }

    fun checkoutCheckedItems(userId: Int, onComplete: () -> Unit = {}) {
        val itemsToCheckout = _cartItems.value.filter { it.isChecked }
        if (itemsToCheckout.isEmpty() || isCheckingOut.value) return

        viewModelScope.launch {
            isCheckingOut.value = true
            var successCount = 0
            try {
                for (item in itemsToCheckout) {
                    val response = RetrofitInstance.api.createBooking(
                        mapOf(
                            "user_id" to userId,
                            "test_id" to item.test.id,
                            "booking_date" to item.bookingDate,
                            "booking_time" to item.bookingTime,
                            "clinic_name" to item.clinicName,
                            "status" to "Menunggu",
                            "result_status" to "Menunggu Hasil",
                            "referral_photo" to if (item.hasDoctorReferral) "present" else null
                        )
                    )
                    if (response.isSuccessful) {
                        successCount++
                    }
                }
                if (successCount > 0) {
                    _cartItems.value = _cartItems.value.filter { !it.isChecked }
                    showCheckoutSuccessModal.value = true
                    onComplete()
                } else {
                    toastMessage.value = "Gagal memproses checkout. Silakan coba lagi."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                toastMessage.value = "Terjadi kesalahan koneksi saat checkout."
            } finally {
                isCheckingOut.value = false
            }
        }
    }

    fun clearToast() {
        toastMessage.value = null
    }

    fun resetSuccessModal() {
        showCheckoutSuccessModal.value = false
    }
}
