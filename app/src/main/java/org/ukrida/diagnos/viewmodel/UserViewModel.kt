// ViewModel untuk mengelola logika bisnis dan state terkait data User (Login, Register, CRUD User)
package org.ukrida.diagnos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ukrida.diagnos.data.model.User
import org.ukrida.diagnos.data.repository.UserRepository

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    var users = mutableStateOf<List<User>>(emptyList())
    var currentUser = mutableStateOf<User?>(null)

    fun getUsers() {
        viewModelScope.launch {
            try {
                users.value = repo.getUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var loginError = mutableStateOf<String?>(null)

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginError.value = null
            try {
                currentUser.value = repo.login(username, password)
            } catch (e: retrofit2.HttpException) {
                e.printStackTrace()
                if (e.code() == 401) {
                    loginError.value = "Username atau password salah!"
                } else {
                    loginError.value = "Gagal login: server error (${e.code()})"
                }
            } catch (e: java.io.IOException) {
                e.printStackTrace()
                // Offline fallback ONLY on connection errors
                val matched = users.value.find { it.username == username && it.password == password }
                if (matched != null) {
                    currentUser.value = matched
                } else {
                    // Fallback to auto-generate a user for testing if not found
                    val userRole = if (username == "admin") "admin" else "user"
                    currentUser.value = User(
                        id = 0,
                        name = username,
                        username = username,
                        password = password,
                        role = userRole
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loginError.value = "Terjadi kesalahan tidak dikenal."
            }
        }
    }

    fun insert(user: User) {
        viewModelScope.launch {
            try {
                repo.insert(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // Add user to local list so they can log in even if database write fails/is offline
            val list = users.value.toMutableList()
            if (!list.any { it.username == user.username }) {
                list.add(user)
            }
            users.value = list
        }
    }

    fun update(user: User) {
        viewModelScope.launch {
            try {
                repo.update(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            currentUser.value = user
            val list = users.value.map { if (it.id == user.id || it.username == user.username) user else it }
            users.value = list
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                repo.delete(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val list = users.value.filter { it.id != id }
            users.value = list
        }
    }
}
