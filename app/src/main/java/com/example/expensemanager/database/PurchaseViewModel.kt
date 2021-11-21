package com.example.expensemanager.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurchaseViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<PurchaseEntity>>
    private val repository: PurchaseRepository

    init{
        val purchaseDao = PurchaseDatabase.getDatabase(application).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
        readAllData = repository.readAllData
    }

    fun addPurchase(purchase: PurchaseEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPurchase(purchase)
        }
    }

    fun updatePurchase(purchase: PurchaseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePurchase(purchase)
        }
    }

    fun deletePurchase(purchase: PurchaseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePurchase(purchase)
        }
    }

    fun deleteAllPurchase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPurchase()
        }
    }
}