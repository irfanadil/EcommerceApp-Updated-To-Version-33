package com.ecommerce.testapp

import android.app.Application
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductRepository @Inject
constructor(@ApplicationContext val application: Application, val loginRepository: LoginRepository) {

    private val cartItemsDao = ProductsDatabase.getDatabase(application).cartItemsDao()
    private val cartItemRepository = CartItemRepository(cartItemsDao)
    private var targetItem:CartItem? = null




    fun getLiveCartItems():LiveData<List<CartItem>> = cartItemRepository.allCartItems

    suspend fun insertUpdateCartItem(cartItem: CartItem)
    {
        val cartItemList = cartItemRepository.getAllCartItems()
        targetItem = null
        for (listItem in cartItemList) {
            if (listItem.cartItemId == cartItem.cartItemId && listItem.cartItemSize == cartItem.cartItemSize) {
                targetItem = listItem
                break
            }
        }
        if (targetItem == null)
            cartItemRepository.insert(cartItem)
        else {
            targetItem!!.cartItemQuantity++
            targetItem!!.cartItemPrice = targetItem!!.cartItemPrice + cartItem.cartItemPrice
            cartItemRepository.updateCartItem(targetItem!!)
        }
    }

    suspend fun updateCartItem(cartItem: CartItem)
    {
        cartItem.cartItemPrice = cartItem.cartItemPrice - (cartItem.cartItemPrice / cartItem.cartItemQuantity)
        cartItem.cartItemQuantity = cartItem.cartItemQuantity - 1
        if (cartItem.cartItemQuantity == 0) {
            cartItemRepository.deleteCartItemById(cartItem.autoId)
            return
        }
        cartItemRepository.updateCartItem(cartItem)
    }

    suspend fun addCartItem(cartItem: CartItem)
    {
        cartItem.cartItemPrice = cartItem.cartItemPrice + (cartItem.cartItemPrice / cartItem.cartItemQuantity)
        cartItem.cartItemQuantity = cartItem.cartItemQuantity + 1
        cartItemRepository.updateCartItem(cartItem)
    }

    suspend fun userLogout()
    {
        loginRepository.logout()
    }


}