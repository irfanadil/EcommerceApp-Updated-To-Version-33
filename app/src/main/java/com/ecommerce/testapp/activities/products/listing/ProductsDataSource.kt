package com.ecommerce.testapp


import android.content.SharedPreferences
import android.util.Log
import com.ecommerce.testapp.activities.products.listing.TestProducts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import javax.inject.Inject


class ProductsDataSource @Inject constructor(private val testAPI: TestAPI, private val sharedPreferences: SharedPreferences) {


    suspend fun getAllProducts() = testAPI.getAllProducts()

    suspend fun getAllProducts2(): ProductsApiState {
        return try {
            val allProducts = testAPI.getAllProducts()
            ProductsApiState.Success(allProducts)
        } catch (e: Throwable) {
            if(e is HttpException) {
                if (e.code() == 401) {
                    // We've got HTTP 401 Unauthorized
                    Log.e("Unauthorized - ", e.toString()  )
                }
            }
            ProductsApiState.Failure(IOException("Error logging in", e))
        }
    }


    suspend fun getAllProductsResponseBody(): ApiResult<AllProductResponse> {
        return try {
            val allProducts = testAPI.getAllProductsResponse()
            Log.e("TAF", allProducts.toString())

            Log.e("TAF", allProducts.toString())
            Log.e("ResponseBody",   allProducts.string())
            Log.e("ResponseBody", allProducts.charStream().toString())
            Log.e("ResponseBody", allProducts.bytes().toString())

            val collectionType: Type = object : TypeToken<Collection<AllProductResponse>>() {}.type
            val gson = Gson()
            val enums = Gson().fromJson<List<TestProducts>>(allProducts.toString())
            Log.e("enums", enums.toString())


            val enums2 = Gson().fromJson<AllProductResponse>(allProducts.toString())
            Log.e("enums2", enums2.toString())

            ApiResult.Success(AllProductResponse())
        } catch (e: Throwable) {
            if(e is HttpException) {
                if (e.code() == 401) {
                    // We've got HTTP 401 Unauthorized
                    Log.e("Unauthorized - ", e.toString()  )
                }
            }
            ApiResult.ErrorException(IOException("Error logging in", e))
        }
    }

}

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)!!

inline fun <reified T: GsonConverterFactory> String.toObject(): T = Gson().fromJson(this, T::class.java)