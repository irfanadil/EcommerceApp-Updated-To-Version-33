package com.ecommerce.testapp

import retrofit2.Call
import retrofit2.http.*

interface TestAPI{

    @GET("user-list")
    suspend fun fetchUserList(): UserListResult

    @POST("authenticate")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("item-list")
    suspend fun fetchItemList( @Header("Authorization")  authHeader:String): ItemListResult


    @POST("authenticate")
    fun loginWithCallBack(@Body loginRequest: LoginRequest): Call<LoginResponse>

}