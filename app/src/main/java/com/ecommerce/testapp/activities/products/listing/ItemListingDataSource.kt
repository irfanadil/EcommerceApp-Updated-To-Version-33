package com.ecommerce.testapp

import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ItemListingDataSource @Inject constructor(private val testAPI: TestAPI, private val sharedPreferences: SharedPreferences) {

    suspend fun fetchAllItems(): Result<ItemListResult> {
        return try {
            val newToken = sharedPreferences.getString(MyAppConfigConstant.TOKEN, "")
            val itemListResult = testAPI.fetchItemList("Bearer $newToken")
            Result.Success(itemListResult)
        } catch (e: Throwable) {
            if(e is HttpException) {
                if (e.code() == 401) {
                    // We've got HTTP 401 Unauthorized
                    Log.e("Unauthorized - ", e.toString()  )
                }
            }
            Result.Error(IOException("Error logging in", e))
        }
    }
}