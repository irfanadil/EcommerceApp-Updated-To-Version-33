package com.ecommerce.testapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(application: Application, loginRepository: LoginRepository)
    : AndroidViewModel(application) {

    private val _product: MutableLiveData<Product> =  MutableLiveData()

    var logoutBool: MutableLiveData<Boolean> = MutableLiveData()

    private val productRepository = ProductRepository(application, loginRepository)

    val product:LiveData<Product> = _product

    fun loadItem(product: Product) {
        _product.value = product
    }

    val cartItems: LiveData<List<CartItem>> = productRepository.getLiveCartItems()

    fun insertUpdateCartItem(cartItem: CartItem)
    {
        viewModelScope.launch {
            try {
                productRepository.insertUpdateCartItem(cartItem)
            } catch (e: Exception) { }
        }
    }

    fun addCartItem(cartItem: CartItem)
    {
        viewModelScope.launch {
            try {
                 productRepository.addCartItem(cartItem)
            } catch (e: Exception) { }
        }
    }

    fun updateCartItem(cartItem: CartItem)
    {
        viewModelScope.launch {
            try {
                productRepository.updateCartItem(cartItem)
            } catch (e: Exception) { }
        }
    }

    fun logoutUser()
    {
        viewModelScope.launch() {
            try {
                withContext(Dispatchers.IO) {
                    productRepository.userLogout()
                }
                Log.e("LogOut C - ", "logoutCompleted.")
                logoutBool.value = true
            } catch (e: Exception) { }
        }
    }

}