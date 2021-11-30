package com.lf.esharing.database.purchase

import androidx.lifecycle.LiveData
import java.util.*

class PurchaseRepository(private val purchaseDao: PurchaseDao, private val purchaseApi: PurchaseApi = PurchaseClient.getInstance()) {
    val readAllData: LiveData<List<PurchaseEntity>> = purchaseDao.readAll()

    suspend fun getPurchaseInfoOfMember(id: UUID, username: String, password: String, member: String): PurchaseEntity? {
        // add to online first then update local
        return purchaseApi.getPurchaseInfoOfMember(id,member, mapOf("username" to username, "password" to password)).body()
    }

    // get locally
    suspend fun getPurchaseById(id: UUID): LiveData<PurchaseEntity>{
        // add to online first then update local
        return purchaseDao.findById(id)
    }

    suspend fun addPurchase(purchase: PurchaseEntity, username: String, password: String){
        // add to online first then update local
        if (purchaseApi.insertPurchase(PurchasesRequest(username, password, purchase)).isSuccessful)
        {
            purchaseDao.insert(purchase)
        }
    }

    suspend fun updatePurchase(purchase: PurchaseEntity, username: String, password: String) {
        if (purchaseApi.updatePurchase(purchase.id, PurchasesRequest(username, password, purchase)).isSuccessful)
        {
            purchaseDao.update(purchase)
        }
    }

    suspend fun deletePurchase(purchase: PurchaseEntity, username: String, password: String) {
        if (purchaseApi.deletePurchase(purchase.id, PurchasesRequest(username, password, purchase)).isSuccessful)
        {
            purchaseDao.delete(purchase)
        }
    }

    suspend fun deleteAllPurchase(username: String, password: String) {
        if (purchaseApi.deleteAll(mapOf("username" to username, "password" to password)).isSuccessful)
        {
            purchaseDao.deleteAll()
        }
    }

    suspend fun deleteAllLocalPurchase() {
        purchaseDao.deleteAll()
    }

    fun insertLocalPurchase(purchases: List<PurchaseEntity>) {
        purchaseDao.insertAll(purchases)
    }
}