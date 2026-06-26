// Model data untuk entitas Tes Laboratorium (Lab Test)
package org.ukrida.diagnos.data.model

data class LabTest(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val priceVal: Int,
    val category: String,
    val duration: String,
    val biomarkerCount: Int,
    val resultDuration: String,
    val benefits: String,
    val fullDescription: String,
    val preparations: List<String>,
    val isPuasa: Boolean,
    val themeColorHex: Long,
    val imageRes: Int
)
