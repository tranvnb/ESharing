package com.lf.esharing.database.purchase

import androidx.lifecycle.LiveData

class PurchaseRepository(private val purchaseDao: PurchaseDao) {
    val readAllData: LiveData<List<PurchaseEntity>> = purchaseDao.readAll()

    fun addPurchase(purchase: PurchaseEntity){
        purchaseDao.add(purchase)
    }

    fun updatePurchase(purchase: PurchaseEntity) {
        purchaseDao.update(purchase)
    }

    fun deletePurchase(purchase: PurchaseEntity) {
        purchaseDao.delete(purchase)
    }

    fun deleteAllPurchase() {
        purchaseDao.deleteAll()
    }
}