package com.lf.esharing.database.purchase

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(purchase: PurchaseEntity)

    /// TODO: be careful with this, might cause losing data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(purchases: List<PurchaseEntity>)

    @Delete
    suspend fun delete(purchase: PurchaseEntity)

    @Delete
    suspend fun deletePurchases(vararg users: PurchaseEntity)

    @Query("DELETE FROM purchases")
    suspend fun deleteAll()

    @Update
    suspend fun update(purchase: PurchaseEntity)

    @Query("SELECT * from purchases WHERE id = :id")
    fun findById(id: UUID): LiveData<PurchaseEntity>

    @Query("SELECT * from purchases ORDER BY id ASC")
    fun readAll(): LiveData<List<PurchaseEntity>>

}