package com.ecommerce.testapp

import androidx.room.Database
import androidx.room.RoomDatabase

/*
@Database(entities = [DatabaseItem::class, CartItem::class ], version = 1, exportSchema = false)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun databaseItemDao(): DatabaseItemDao

    abstract fun cartItemsDao(): CartItemsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ProductsDatabase? = null

        fun getDatabase(context: Context): ProductsDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ProductsDatabase::class.java,
                    AppConstant.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
*/

@Database(entities = [DatabaseItem::class, CartItem::class ], version = 1, exportSchema = false)
abstract class EcommerceDatabase : RoomDatabase(){
    abstract fun databaseItemDao(): DatabaseItemDao

    abstract fun cartItemsDao(): CartItemsDao
}