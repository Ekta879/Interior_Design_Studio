package com.example.interiordeisgnstudio1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.interiordeisgnstudio1.Model.UserModel
import com.example.interiordeisgnstudio1.repository.UserRepo

class UserViewModel(private val repo: UserRepo) : ViewModel() {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }

    fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        repo.register(email, password, callback)
    }

    fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addUserToDatabase(userId, model, callback)
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteAccount(userId, callback)
    }

    fun editProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repo.editProfile(userId, model, callback)
    }

    private val _user = MutableLiveData<UserModel?>()
    val user get() = _user

    private val _allUsers = MutableLiveData<List<UserModel>?>()
    val allUsers get() = _allUsers

    fun getUserById(userId: String) {
        repo.getUserById(userId) { success, _, data ->
            if (success) _user.postValue(data)
        }
    }

    fun getAllUsers() {
        repo.getAllUser { success, _, data ->
            if (success) _allUsers.postValue(data)
        }
    }
}