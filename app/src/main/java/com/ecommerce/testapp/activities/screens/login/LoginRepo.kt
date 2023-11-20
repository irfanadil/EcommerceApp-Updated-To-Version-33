package com.ecommerce.testapp

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class LoginRepo @Inject
constructor(
    private val dataSource: LoginDataSource, private val spEditor: SharedPreferences.Editor,
    @ApplicationContext val applicationContext: Context
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

        spEditor.putBoolean(MyAppConfigConstant.IS_LOGGED_IN, true).apply()
        spEditor.putString(MyAppConfigConstant.TOKEN, loginResponse.token).apply()
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }


    fun logout() {
        spEditor.clear()
        spEditor.putBoolean(MyAppConfigConstant.IS_LOGGED_IN, false).apply()
        spEditor.putString(MyAppConfigConstant.TOKEN, "").apply()
        ProductsDatabase.getDatabase(applicationContext) .clearAllTables()
        //dataSource.logout()
    }

}