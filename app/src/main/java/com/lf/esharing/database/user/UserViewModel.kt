package com.lf.esharing.database.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lf.esharing.database.AppDatabase
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()

    val userRepository: UserRepository = UserRepository(userDao)

    fun login(username: String, password: String): MutableLiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()
        viewModelScope.launch {
            result.postValue(userRepository.login(username, password))
        }
        return result
    }

    fun getUsers(): MutableLiveData<List<UserEntity>?> {
        val result: MutableLiveData<List<UserEntity>?> = MutableLiveData()
        viewModelScope.launch {
            result.postValue(userRepository.getUsers())
        }
        return result
    }
}