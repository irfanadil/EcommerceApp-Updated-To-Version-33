package com.ecommerce.testapp

import androidx.lifecycle.LiveData

class CartItemRepository(private val cartItemsDao: CartItemsDao) {

    val allCartItems: LiveData<List<CartItem>> = cartItemsDao.getLiveCartItems()

    suspend fun getAllCartItems():List<CartItem> {
        return cartItemsDao.getCartItems()
    }

    //suspend fun getCartItemById(id:Int):CartItem?
    //{
        //return cartItemsDao.getCartItemById(id)
    //}

    suspend fun deleteCartItemById(id:Int):Int?
    {
        return cartItemsDao.deleteCartItemById(id)
    }

    suspend fun deleteAllCartItems():Unit  =  cartItemsDao.deleteAll()

    suspend fun insert(cartItem: CartItem) {
        cartItemsDao.insert(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItem)
    {
        cartItemsDao.update(cartItem.cartItemQuantity, cartItem.cartItemPrice,  cartItem.autoId)
    }

}