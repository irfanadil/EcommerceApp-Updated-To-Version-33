package com.ecommerce.testapp

import java.lang.Exception

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
/*
sealed class ApiResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val exception: String) : ApiResult<Nothing>()
    data class Loading(val loadingMsg: String) : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            else -> {
                "Unknown Exception"
            }
        }
    }
}


sealed class GenericApiResponse<out T> {

    class Success<out T>(val value: T) : GenericApiResponse<T>()

    class Failure(
        val failureStatus: FailureStatus = FailureStatus.API_FAIL,
        val code: Int? = null,
        val message: String? = null
    ) : GenericApiResponse<Nothing>()

    object Loading : GenericApiResponse<Nothing>()

    object Default : GenericApiResponse<Nothing>()
}

enum class FailureStatus {
    EMPTY,
    API_FAIL,
    NO_INTERNET,
    OTHER
}


sealed class UrlViewState {
    object Empty : UrlViewState()
    object Loading : UrlViewState()
    data class Success(val urls: List<UrlResultEntity>) : UrlViewState()
    data class Error(val exception: Throwable) : UrlViewState()
}

 */


sealed class ApiResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class ErrorString(val exception: String) : ApiResult<Nothing>()
    data class ErrorException(val exception: Exception) : ApiResult<Nothing>()
    data class Loading(val loadingMsg: String) : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ErrorString -> "Error[exception=$exception]"
            else -> {
                "Unknown Exception"
            }
        }
    }
}


sealed class LoginApiResult {
    //data class ErrorString(val message: String) : LoginApiResult()
    //data class ErrorException(val exception: Exception) : LoginApiResult()
    data class Success(val data: LoginResponse) : LoginApiResult()
    data class Loading(val loadingMsg: String) : LoginApiResult()
    class Failure(
        val failureStatus: FailureStatus = FailureStatus.API_FAIL,
        val code: Int? = null,
        val exception: Exception? = null,
        val message: String? = null ) : LoginApiResult()
}

enum class FailureStatus {
    EMPTY,
    API_FAIL,
    NO_INTERNET,
    OTHER
}


sealed class ProductsApiState{
    object Loading : ProductsApiState()
    class Failure(val msg:Throwable) : ProductsApiState()
    class Success(val data:List<Product>) : ProductsApiState()
    object Empty : ProductsApiState()
}
