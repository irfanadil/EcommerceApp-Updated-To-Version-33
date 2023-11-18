package com.ecommerce.testapp

import com.ecommerce.testapp.activities.products.listing.TestProducts
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

// Test api used from -  https://fakestoreapi.com/
interface TestAPI{


    @GET("user-list")
    suspend fun fetchUserList(): UserListResult

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse


    @POST("auth/login")
    suspend fun loginResponseBody(@Body loginRequest: LoginRequest): ResponseBody

    @GET("products")
    suspend fun getAllProducts(): List<Product>


    @GET("products")
    suspend fun getAllProductsResponse(): ResponseBody



    @POST("authenticate")
    fun loginWithCallBack(@Body loginRequest: LoginRequest): Call<LoginResponse>

}