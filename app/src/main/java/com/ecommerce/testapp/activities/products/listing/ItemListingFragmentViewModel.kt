package com.golfercard.playsafe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.testapp.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListingFragmentViewModel  @Inject constructor(private val itemListingRepository: ItemListingRepository) : ViewModel() {

    private val _itemListResult = MutableLiveData<ItemListResult>()

    val itemListResult: LiveData<ItemListResult> = _itemListResult

    fun refresh() = fetchAllItems()

    private fun fetchAllItems() {
        viewModelScope.launch {
            try {

                //delay(4000) // for testing purpose...


                val result = itemListingRepository.fetchAllItems()
                Log.e("result - ", result.toString())
                if (result is Result.Success)
                    _itemListResult.value = result.data
                else
                    _itemListResult.value = ItemListResult()
                //updateUi()
            } catch (e: Exception) {
                _itemListResult.value = ItemListResult()
                Log.e("error ", "" + e.printStackTrace())
            }
        }
    }

}