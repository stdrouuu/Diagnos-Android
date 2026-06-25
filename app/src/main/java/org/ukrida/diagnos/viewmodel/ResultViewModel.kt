package org.ukrida.diagnos.viewmodel

import androidx.lifecycle.ViewModel
import org.ukrida.diagnos.data.model.TestParameterResult

class ResultViewModel : ViewModel() {

    fun getDarahRutinList(): List<TestParameterResult> {
        return listOf(
            TestParameterResult("Hemoglobin", "14.8", "11.7 - 15.5", "g/dL", "Perempuan, Dewasa"),
            TestParameterResult("Hematokrit", "44.5", "35 - 47", "%", "Perempuan, Dewasa"),
            TestParameterResult("Eritrosit", "4.89", "3.8 - 5.2", "10^6/uL", "Perempuan, Dewasa")
        )
    }

    fun getNilaiMcList(): List<TestParameterResult> {
        return listOf(
            TestParameterResult("MCV", "91.0", "80 - 100", "fL", "Dewasa", isBullet = true),
            TestParameterResult("MCH", "30.3", "26 - 34", "pg", "Dewasa", isBullet = true),
            TestParameterResult("MCHC", "33.3", "32 - 36", "g/dL", "5 tahun - Dewasa", isBullet = true),
            TestParameterResult("RDW-CV", "13.1", "11.5 - 14.5", "%", "Dewasa"),
            TestParameterResult("Trombosit", "296", "150 - 440", "10^3/uL", "Dewasa"),
            TestParameterResult("Leukosit", "5.5", "3.6 - 11", "10^3/uL", "Perempuan, Dewasa")
        )
    }

    fun getHitungJenisLeukositList(): List<TestParameterResult> {
        return listOf(
            TestParameterResult("Basofil", "0.4", "0.0 - 1.0", "%", "", isBullet = true),
            TestParameterResult("Eosinofil", "1.8* \u2193", "2.0 - 4.0", "%", "Dewasa", isOut = true, isBullet = true),
            TestParameterResult("Neutrofil", "75.2* \u2191", "50.0 - 70.0", "%", "Dewasa", isOut = true, isBullet = true),
            TestParameterResult("Limfosit", "17.7* \u2193", "25.0 - 40.0", "%", "Dewasa", isOut = true, isBullet = true),
            TestParameterResult("Monosit", "4.9", "2.0 - 8.0", "%", "Dewasa", isBullet = true),
            TestParameterResult("LED", "17", "0 - 30", "mm/jam", "Perempuan, > 50\ntahun")
        )
    }
}
