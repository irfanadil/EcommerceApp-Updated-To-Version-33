package com.ecommerce.testapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartItemsDao {

    @Query("SELECT * from cartTable ORDER BY cartItemId ASC")
    fun getLiveCartItems(): LiveData<List<CartItem>>

    @Query("SELECT * from cartTable ORDER BY cartItemId ASC")
    suspend fun getCartItems(): List<CartItem>

    //@Query("SELECT * FROM cartTable WHERE cartItemId=:id")
    //suspend fun getCartItemById(id: Int): CartItem


    @Query("DELETE FROM cartTable WHERE autoId=:id")
    suspend fun deleteCartItemById(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Query("UPDATE cartTable SET cartItemQuantity=:quantity , cartItemPrice=:price WHERE autoId=:id")
    suspend fun update(quantity:Int, price:Int,  id:Int)


    //@WorkerThread
    @Query("DELETE FROM cartTable")
    suspend fun deleteAll()
}