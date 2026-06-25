package org.ukrida.diagnos.data.model

data class TestParameterResult(
    val name: String,
    val resultValue: String,
    val referenceRange: String,
    val unit: String,
    val note: String,
    val isOut: Boolean = false, // if true, draws in red
    val isBullet: Boolean = false // if true, prepends bullet indent
)
