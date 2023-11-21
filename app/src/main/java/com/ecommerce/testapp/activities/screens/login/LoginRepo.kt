package com.ecommerce.testapp

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class LoginRepo @Inject
constructor(
    private val dataSource: LoginDataSource, private val spEditor: SharedPreferences.Editor,
    @ApplicationContext val applicationContext: Context,
    private val ecommerceDatabase: EcommerceDatabase
)
{

    private var user: LoginResponse? = null
    val isLoggedIn: Boolean
        get() = user != null


    suspend fun login(username: String, password: String): LoginApiResult {
        val result = dataSource.loginByResponseBody(username, password)
        if (result is LoginApiResult.Success)
            setLoggedInUser(result.data)
        return result
    }

    private fun setLoggedInUser(loginResponse: LoginResponse) {

        spEditor.putBoolean(AppConstant.IS_LOGGED_IN, true).apply()
        spEditor.putString(AppConstant.TOKEN, loginResponse.token).apply()
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }


    fun logout() {
        spEditor.clear()
        spEditor.putBoolean(AppConstant.IS_LOGGED_IN, false).apply()
        spEditor.putString(AppConstant.TOKEN, "").apply()
        ecommerceDatabase.clearAllTables()
        //ProductsDatabase.getDatabase(applicationContext) .clearAllTables()
        //dataSource.logout()
    }

}