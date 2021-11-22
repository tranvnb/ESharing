package com.lf.esharing.database.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    // be careful with this, might cause losing data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Delete
    suspend fun detele(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * from users WHERE id = :id")
    fun findById(id: Int): LiveData<UserEntity>

}