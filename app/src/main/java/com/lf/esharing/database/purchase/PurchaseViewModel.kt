package com.lf.esharing.database.purchase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lf.esharing.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class PurchaseViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<PurchaseEntity>>
    private val repository: PurchaseRepository

    init{
        val purchaseDao = AppDatabase.getDatabase(application).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
        readAllData = repository.readAllData
    }

    fun addPurchase(request: RequestBody, purchase: PurchaseEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPurchase(request, purchase)
        }
    }

    fun updatePurchase(purchase: PurchaseEntity, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePurchase(purchase, username, password)
        }
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
}