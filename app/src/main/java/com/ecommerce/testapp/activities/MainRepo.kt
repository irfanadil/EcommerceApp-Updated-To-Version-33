package com.ecommerce.testapp.activities

import android.content.Context
import androidx.lifecycle.LiveData
import com.ecommerce.testapp.CartItem
import com.ecommerce.testapp.CartItemRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainRepo @Inject
constructor(
    @ApplicationContext private val appContext: Context,
            private val cartItemRepo:CartItemRepo
            //val loginRepository: LoginRepository,
            //private val dataSource: LoginDataSource, private val spEditor: SharedPreferences.Editor,
) {


    private var targetItem: CartItem? = null

    fun getLiveCartItems():LiveData<List<CartItem>> = cartItemRepo.allCartItems

    suspend fun insertUpdateCartItem(cartItem: CartItem)
    {
        val cartItemList = cartItemRepo.getAllCartItems()
        targetItem = null
        for (listItem in cartItemList) {
            if (listItem.cartItemId == cartItem.cartItemId) {
                targetItem = listItem
                break
            }
        }
        if (targetItem == null)
            cartItemRepo.insert(cartItem)
        else {
            targetItem!!.cartItemQuantity++
            targetItem!!.cartItemPrice = targetItem!!.cartItemPrice + cartItem.cartItemPrice
            cartItemRepo.updateCartItem(targetItem!!)
        }
    }

    suspend fun updateCartItem(cartItem: CartItem)
    {
        cartItem.cartItemPrice = cartItem.cartItemPrice - (cartItem.cartItemPrice / cartItem.cartItemQuantity)
        cartItem.cartItemQuantity = cartItem.cartItemQuantity - 1
        if (cartItem.cartItemQuantity == 0) {
            cartItemRepo.deleteCartItemById(cartItem.autoId)
            return
        }
        cartItemRepo.updateCartItem(cartItem)
    }

    suspend fun addCartItem(cartItem: CartItem)
    {
        cartItem.cartItemPrice = cartItem.cartItemPrice + (cartItem.cartItemPrice / cartItem.cartItemQuantity)
        cartItem.cartItemQuantity = cartItem.cartItemQuantity + 1
        cartItemRepo.updateCartItem(cartItem)
    }






}