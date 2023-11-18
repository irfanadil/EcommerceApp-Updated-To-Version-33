package com.ecommerce.testapp

import android.util.Log
import com.ecommerce.testapp.activities.products.listing.TestProducts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject

class LoginDataSource @Inject constructor(private val testAPI: TestAPI){

    suspend fun login(username: String, password: String): LoginApiResult {
        return try {
            val loginResponseBody = testAPI.login(LoginRequest(username, password))

            //val collectionType = object : TypeToken<LoginApiResult>() {}.type
            //val result1 = Gson().fromJson<LoginApiResult>(loginResponseBody.toString(), collectionType)




            Log.e("TAF" , loginResponseBody.toString())

            LoginApiResult.Success(loginResponseBody)
        } catch (e: Throwable) {
            LoginApiResult.Failure(
                FailureStatus.API_FAIL,
                message = "Api fail exception" , exception = IOException("Error logging in", e)
            )
        }
    }

    /*
    suspend fun fetchUserList(): ApiResult<UserListResult> {
        return try {
             ApiResult.Success(testAPI.fetchUserList())
        } catch (e: Throwable) {
            ApiResult.Error(IOException("Error logging in", e))
        }
    }
    */

}