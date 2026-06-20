package org.ukrida.diagnos.data.model

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val password: String,
    val role: String = "user",
    val photo: String? = null
)
