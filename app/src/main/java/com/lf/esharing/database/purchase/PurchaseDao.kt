package com.lf.esharing.database.purchase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(purchase: PurchaseEntity)

    /// TODO: be careful with this, might cause losing data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(purchases: List<PurchaseEntity>)

    @Delete
    fun delete(purchase: PurchaseEntity)

    @Query("DELETE FROM purchases")
    fun deleteAll()

    @Update
    fun update(purchase: PurchaseEntity)

    @Query("SELECT * from purchases WHERE id = :id")
    fun findById(id: Int): LiveData<PurchaseEntity>

    @Query("SELECT * from purchases ORDER BY id ASC")
    fun readAll(): LiveData<List<PurchaseEntity>>

}