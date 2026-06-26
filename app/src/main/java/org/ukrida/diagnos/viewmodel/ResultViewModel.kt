package org.ukrida.diagnos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ukrida.diagnos.data.model.TestParameterResult

class ResultViewModel : ViewModel() {

    var currentResultsMap = mutableStateOf<Map<String, String?>?>(null)

    fun loadResults(bookingId: Int) {
        viewModelScope.launch {
            try {
                currentResultsMap.value = org.ukrida.diagnos.data.api.RetrofitInstance.api.getLabResults(bookingId)
            } catch (e: Exception) {
                e.printStackTrace()
                currentResultsMap.value = emptyMap()
            }
        }
    }

    private fun buildParameter(
        name: String,
        dbKey: String,
        refRange: String,
        unit: String,
        keterangan: String,
        isBullet: Boolean = false
    ): TestParameterResult {
        val map = currentResultsMap.value
        if (map == null) {
            return TestParameterResult(name, "-", refRange, unit, keterangan, isBullet = isBullet)
        }
        val rawVal = map[dbKey]
        if (rawVal.isNullOrEmpty() || rawVal == "-") {
            return TestParameterResult(name, "-", refRange, unit, keterangan, isBullet = isBullet)
        }

        val cleanVal = rawVal.trim()
        val valueDouble = cleanVal.toDoubleOrNull()
        var isOut = false
        var displayVal = cleanVal

        if (valueDouble != null) {
            val rangeParts = refRange.split("-")
            if (rangeParts.size == 2) {
                val min = rangeParts[0].trim().toDoubleOrNull()
                val max = rangeParts[1].trim().toDoubleOrNull()
                if (min != null && max != null) {
                    if (valueDouble < min) {
                        isOut = true
                        displayVal = "$cleanVal* \u2193"
                    } else if (valueDouble > max) {
                        isOut = true
                        displayVal = "$cleanVal* \u2191"
                    }
                }
            }
        }

        return TestParameterResult(name, displayVal, refRange, unit, keterangan, isOut = isOut, isBullet = isBullet)
    }

    fun getDarahRutinList(): List<TestParameterResult> {
        return listOf(
            buildParameter("Hemoglobin", "hemoglobin", "11.7 - 15.5", "g/dL", "Perempuan, Dewasa"),
            buildParameter("Hematokrit", "hematokrit", "35 - 47", "%", "Perempuan, Dewasa"),
            buildParameter("Eritrosit", "eritrosit", "3.8 - 5.2", "10^6/uL", "Perempuan, Dewasa"),
            buildParameter("Trombosit", "trombosit", "150 - 440", "10^3/uL", "Dewasa"),
            buildParameter("RDW-CV", "rdw_cv", "11.5 - 14.5", "%", "Dewasa"),
            buildParameter("Leukosit", "leukosit", "3.6 - 11", "10^3/uL", "Perempuan, Dewasa"),
            buildParameter("LED", "led", "0 - 30", "mm/jam", "Perempuan, > 50\ntahun")
        )
    }

    fun getNilaiMcList(): List<TestParameterResult> {
        return listOf(
            buildParameter("MCV", "mcv", "80 - 100", "fL", "Dewasa", isBullet = true),
            buildParameter("MCH", "mch", "26 - 34", "pg", "Dewasa", isBullet = true),
            buildParameter("MCHC", "mchc", "32 - 36", "g/dL", "5 tahun - Dewasa", isBullet = true)
        )
    }

    fun getHitungJenisLeukositList(): List<TestParameterResult> {
        return listOf(
            buildParameter("Basofil", "basofil", "0.0 - 1.0", "%", "", isBullet = true),
            buildParameter("Eosinofil", "eosinofil", "2.0 - 4.0", "%", "Dewasa", isBullet = true),
            buildParameter("Neutrofil", "neutrofil", "50.0 - 70.0", "%", "Dewasa", isBullet = true),
            buildParameter("Limfosit", "limfosit", "25.0 - 40.0", "%", "Dewasa", isBullet = true),
            buildParameter("Monosit", "monosit", "2.0 - 8.0", "%", "Dewasa", isBullet = true)
        )
    }
}
