package com.ecommerce.testapp

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class LoginRepository @Inject
constructor(
    private val dataSource: LoginDataSource, private val spEditor: SharedPreferences.Editor,
    @ApplicationContext val applicationContext: Context
)
{

    private var user: LoginResponse? = null
    val isLoggedIn: Boolean
        get() = user != null


    init {
        //MyApp.appComponent.inject(this)

        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore


        /*
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        val masterKey = MasterKey.Builder(applicationContext)
            .setKeyGenParameterSpec(spec)
            .build()

        val sharedPreferences =EncryptedSharedPreferences.create(
           applicationContext,
            "FILE_NAME",
            masterKey, // masterKey created above
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
         */

        /*
        Depreciated way...

        // Although you can define your own key generation parameter specification, it's
        // recommended that you use the value specified here.
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val sharedPreferences = EncryptedSharedPreferences
            .create(
                "FILE_NAME",
                masterKeyAlias,
                applicationContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        val sharedPrefsEditor = sharedPreferences.edit()

         */

        user = null
    }

    suspend fun fetchUserList(): Result<UserListResult> {
        return dataSource.fetchUserList()
    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        val result = dataSource.login(username, password)
        if (result is Result.Success)
            setLoggedInUser(result.data)
        return result
    }

    private fun setLoggedInUser(loginResponse: LoginResponse) {
        this.user = loginResponse
        spEditor.putBoolean(MyAppConfigConstant.IS_LOGGED_IN, true).apply()
        spEditor.putString(MyAppConfigConstant.TOKEN, loginResponse.token).apply()
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    suspend fun logout() {
        user = null
        spEditor.clear()
        spEditor.putBoolean(MyAppConfigConstant.IS_LOGGED_IN, false).apply()
        spEditor.putString(MyAppConfigConstant.TOKEN, "").apply()
        ProductsDatabase.getDatabase(applicationContext) .clearAllTables()


        //dataSource.logout()
    }

}