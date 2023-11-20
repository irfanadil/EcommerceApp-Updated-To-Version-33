package com.ecommerce.testapp

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject

class LoginDataSource @Inject constructor(private val testAPI: TestAPI){

    suspend fun login(username: String, password: String): LoginApiResult {
        return try {
            LoginApiResult.Success(testAPI.login(LoginRequest(username, password)))
        } catch (e: Throwable) {
            LoginApiResult.Failure(
                FailureStatus.API_FAIL,
                message = "Api fail exception" , exception = IOException("Error logging in", e)
            )
        }
    }

    suspend fun loginByResponseBody(username: String, password: String): LoginApiResult {
        return try {
            val loginResponseBody = testAPI.loginResponseBody(LoginRequest(username, password))
            // this value can be consumed only once, if you call loginResponseBody.string() again, it will return blank
            // https://stackoverflow.com/questions/28300359/cant-get-okhttps-response-body-tostring-to-return-a-string

            var content = loginResponseBody.string()
            Log.e("responseTypeCasted " ,content) // will work fine...

            //content = loginResponseBody.string()
            //Log.e("responseTypeCasted2= " ,content) // will be empty string...

            val token: TypeToken<LoginResponse> = object : TypeToken<LoginResponse>() {}
            val loginResponseTypeCasted :LoginResponse  = Gson().fromJson(content, token.type)
            Log.e("responseTypeCasted " , loginResponseTypeCasted.toString())
            LoginApiResult.Success(loginResponseTypeCasted)
        } catch (e: Throwable) {
            LoginApiResult.Failure(
                FailureStatus.API_FAIL,
                message = "Api fail exception" , exception = IOException("Error logging in", e)
            )
        }
    }

}