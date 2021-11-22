package com.lf.esharing.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lf.esharing.database.purchase.PurchaseDao
import com.lf.esharing.database.purchase.PurchaseEntity
import com.lf.esharing.database.user.UserDao
import com.lf.esharing.database.user.UserEntity
import com.lf.esharing.utils.DateConverter
import com.lf.esharing.utils.UUIDConverter

@Database(
    entities = [PurchaseEntity::class, UserEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun purchaseDao(): PurchaseDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "esharing_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}