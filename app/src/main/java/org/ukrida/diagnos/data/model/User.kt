package org.ukrida.diagnos.data.model

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val password: String? = null,
    val role: String = "user",
    val photo: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val dob: String? = null,
    val address: String? = null
)
