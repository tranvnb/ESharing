package com.lf.esharing.database.purchase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lf.esharing.database.AppDatabase
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.utils.MoshiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody

class PurchaseViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PurchaseRepository

    init{
        val purchaseDao = AppDatabase.getDatabase(application).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
    }

    fun addPurchase(request: RequestBody, purchase: PurchaseEntity): MutableLiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO){
            result.postValue(repository.addPurchase(request, purchase))
        }

        return result
    }

    fun updatePurchase(request: RequestBody, purchase: PurchaseEntity): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(repository.updatePurchase(request, purchase))
        }
        return result
    }

    fun deletePurchase(purchase: PurchaseEntity, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePurchase(purchase, username, password)
        }
    }

    fun deleteAllPurchase(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPurchase(username, password)
        }
    }

    fun deleteAllLocalPurchase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllLocalPurchase()
        }
    }

    fun insertLocalPurchase(purchases: List<PurchaseEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLocalPurchase(purchases)
        }
    }

    fun deletePurchases(purchases: List<PurchaseEntity>) {
        viewModelScope.launch {
            val purchasesRequest = PurchasesRequest(UserViewModel.username, UserViewModel.password, null, purchases)
            var str = MoshiHelper.toJsonObject(PurchasesRequest::class.java, purchasesRequest)
            val request = RequestBody.create(MediaType.parse("application/json"), str)
            repository.deletePurchases(request, purchases)
        }
    }

    fun getAllLocalData(): LiveData<List<PurchaseEntity>> {
        return repository.getAllLocalData()
    }
}