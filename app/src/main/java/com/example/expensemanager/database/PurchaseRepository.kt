package com.example.expensemanager.database

import androidx.lifecycle.LiveData

class PurchaseRepository(private val purchaseDao: PurchaseDao) {
    val readAllData: LiveData<List<PurchaseEntity>> = purchaseDao.readAllData()

    fun addPurchase(purchase: PurchaseEntity){
        purchaseDao.addPurchase(purchase)
    }

    fun updatePurchase(purchase: PurchaseEntity) {
        purchaseDao.updatePurchase(purchase)
    }

    fun deletePurchase(purchase: PurchaseEntity) {
        purchaseDao.deletePurchase(purchase)
    }

    fun deleteAllPurchase() {
        purchaseDao.deleteAllPurchase()
    }
}