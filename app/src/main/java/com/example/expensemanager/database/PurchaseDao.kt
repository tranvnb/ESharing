package com.example.expensemanager.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPurchase(purchase: PurchaseEntity)

    @Update
    fun updatePurchase(purchase: PurchaseEntity)

    @Delete
    fun deletePurchase(purchase: PurchaseEntity)

    @Query("DELETE FROM purchase_table")
    fun deleteAllPurchase()

    @Query("SELECT * from purchase_table ORDER BY purchaseId ASC")
    fun readAllData(): LiveData<List<PurchaseEntity>>
}