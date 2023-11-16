package com.ecommerce.testapp

/*
data class LoginResponseResult(
    val success: LoginResponse? = null,
    val error: Int? = null
)
 */

sealed class LoginApiResult {
    data class Error(val message: String) : LoginApiResult()
    data class Success(val data: LoginResponse) : LoginApiResult()
    data class Loading(val loadingMsg: String) : LoginApiResult()
}

data class LoginResponse(val token: String)