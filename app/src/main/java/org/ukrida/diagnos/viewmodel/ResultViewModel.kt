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

    fun getDarahRutinList(gender: String?): List<TestParameterResult> {
        val isMale = gender == "L" || gender?.lowercase()?.contains("laki") == true
        val hbRange = if (isMale) "13.5 - 17.5" else "11.7 - 15.5"
        val htRange = if (isMale) "40 - 52" else "35 - 47"
        val erRange = if (isMale) "4.4 - 5.9" else "3.8 - 5.2"
        val ledRange = if (isMale) "0 - 20" else "0 - 30"
        val genderLabel = if (isMale) "Laki-laki, Dewasa" else "Perempuan, Dewasa"
        val ledLabel = if (isMale) "Laki-laki, > 50\ntahun" else "Perempuan, > 50\ntahun"

        return listOf(
            buildParameter("Hemoglobin", "hemoglobin", hbRange, "g/dL", genderLabel),
            buildParameter("Hematokrit", "hematokrit", htRange, "%", genderLabel),
            buildParameter("Eritrosit", "eritrosit", erRange, "10^6/uL", genderLabel),
            buildParameter("Trombosit", "trombosit", "150 - 440", "10^3/uL", "Dewasa"),
            buildParameter("RDW-CV", "rdw_cv", "11.5 - 14.5", "%", "Dewasa"),
            buildParameter("Leukosit", "leukosit", "3.6 - 11", "10^3/uL", genderLabel),
            buildParameter("LED", "led", ledRange, "mm/jam", ledLabel)
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
