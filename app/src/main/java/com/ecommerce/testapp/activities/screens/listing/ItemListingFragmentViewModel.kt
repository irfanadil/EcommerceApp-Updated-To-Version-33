package com.golfercard.playsafe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.testapp.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListingFragmentViewModel  @Inject constructor(private val productsRepo: ItemListingRepo) : ViewModel() {

    //private val _getProductsResponse = MutableLiveData<AllProductResponse>()

    //val getAllProducts: LiveData<AllProductResponse> = _getProductsResponse

    fun refresh() = fetchAllItems()


    private val _allProductState: MutableStateFlow<ProductsApiState>
            = MutableStateFlow(ProductsApiState.Empty)

    val allProductState: StateFlow<ProductsApiState> = _allProductState


    private fun fetchAllItems() {
        viewModelScope.launch {
            _allProductState.value = ProductsApiState.Loading
            productsRepo.getAllProducts()
                .catch { e ->
                    _allProductState.value = ProductsApiState.Failure(e)
                }.collect { data ->
                    _allProductState.value = ProductsApiState.Success(data)
                }
        }

    }

}

