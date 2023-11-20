package com.ecommerce.testapp.activities.screens

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.testapp.CartItem
import com.ecommerce.testapp.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( @ApplicationContext applicationContext: Context,
                                        private val mainRepo:MainRepo)
    : ViewModel() {

    private val _product: MutableLiveData<Product> =  MutableLiveData()

    val product:LiveData<Product> = _product

    fun loadItem(product: Product) {
        _product.value = product
    }

    val cartItems: LiveData<List<CartItem>> = mainRepo.getLiveCartItems()

    fun insertUpdateCartItem(cartItem: CartItem)
    {
        viewModelScope.launch {
            try {
                mainRepo.insertUpdateCartItem(cartItem)
            } catch (e: Exception) { }
        }
    }

    fun addCartItem(cartItem: CartItem)
    {
        viewModelScope.launch {
            try {
                 mainRepo.addCartItem(cartItem)
            } catch (e: Exception) { }
        }
    }

    fun updateCartItem(cartItem: CartItem)
    {
        viewModelScope.launch {
            try {
                mainRepo.updateCartItem(cartItem)
            } catch (e: Exception) { }
        }
    }






}