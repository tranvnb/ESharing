package com.lf.esharing.database.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lf.esharing.database.AppDatabase
import com.lf.esharing.database.purchase.PurchaseEntity
import com.lf.esharing.utils.MoshiHelper
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class UserViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        var username: String = "";
        var password: String = ""
        var isOwner: Boolean = true
        val currentMembers: MutableLiveData<List<String>> = MutableLiveData()
    }

    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()

    val userRepository: UserRepository = UserRepository(userDao)

    fun login(username: String, password: String): MutableLiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()
        viewModelScope.launch {
            val response = userRepository.login(username, password)
            if (response) {
                // store username and password for future requests
                UserViewModel.username = username
                UserViewModel.password = password
            }
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

    fun getMembers(username: String): MutableLiveData<List<String>?> {
        val result: MutableLiveData<List<String>?> = MutableLiveData()
        viewModelScope.launch {
            result.postValue(userRepository.getMembers(username))
        }
        return result
    }

    fun getPurchases(username: String): MutableLiveData<List<PurchaseEntity>?> {
        val result: MutableLiveData<List<PurchaseEntity>?> = MutableLiveData()
        viewModelScope.launch {
            result.postValue(userRepository.getPurchases(username))
        }
        return result
    }

    fun signup(user: UserEntity): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>();
        viewModelScope.launch {
            val str = MoshiHelper.toJsonObject(UserEntity::class.java, user)
            val request = RequestBody.create(MediaType.parse("application/json"), str)
            val response = userRepository.signup(request)
            if (response) {
                // store username and password for future requests
                UserViewModel.username = user.username
                UserViewModel.password = user.password
            }
            result.postValue(response)
        }
        return result
    }

    fun addMember(member: String): LiveData<JSONObject?> {
        val result = MutableLiveData<JSONObject?>()
        viewModelScope.launch {
            val str = "{\"username\":\"${UserViewModel.username}\", \"password\":\"${UserViewModel.password}\", \"member\":\"$member\"}"
            val request = RequestBody.create(MediaType.parse("application/json"), str)
            val response = userRepository.addMember(request)
            result.postValue(response)
        }
        return result
    }

    fun removeMember(member: String): LiveData<JSONObject?> {
        val result = MutableLiveData<JSONObject?>()
        viewModelScope.launch {
            val str = "{\"username\":\"${UserViewModel.username}\", \"password\":\"${UserViewModel.password}\", \"member\":\"$member\"}"
            val request = RequestBody.create(MediaType.parse("application/json"), str)
            val response = userRepository.removeMember(request)
            result.postValue(response)
        }
        return result
    }
}