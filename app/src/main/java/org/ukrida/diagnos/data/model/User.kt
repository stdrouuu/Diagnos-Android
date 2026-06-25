// Model data untuk entitas User
package org.ukrida.diagnos.data.model

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val password: String,
    val role: String = "user",
    val photo: String? = null,
    val email: String = "",
    val phone: String = "",
    val gender: String = "",
    val dob: String = "",
    val address: String = ""
)
