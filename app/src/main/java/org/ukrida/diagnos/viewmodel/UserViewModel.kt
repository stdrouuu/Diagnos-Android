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
            users.value = repo.getUsers()
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            currentUser.value = repo.login(username, password)
        }
    }

    fun insert(user: User) {
        viewModelScope.launch {
            repo.insert(user)
            getUsers()
        }
    }

    fun update(user: User) {
        viewModelScope.launch {
            repo.update(user)
            getUsers()
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            repo.delete(id)
            getUsers()
        }
    }
}
