package com.ecommerce.testapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

        @Singleton
        @Provides
        fun provideSharedPreferences(  @ApplicationContext context: Context): SharedPreferences {
            //return context.getSharedPreferences(MyAppConfigConstant.APP_PREFERENCES, Context.MODE_PRIVATE)
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()

            val masterKey = MasterKey.Builder(context)
                .setKeyGenParameterSpec(spec)
                .build()

             return EncryptedSharedPreferences.create(
                context,
                 MyAppConfigConstant.APP_PREFERENCES,
                masterKey, // masterKey created above
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        }


        @Singleton
        @Provides
        fun provideSharedPrefsEditor( sharedPreferences: SharedPreferences): SharedPreferences.Editor {
            return sharedPreferences.edit()
        }

        @Singleton
        @Provides
        fun provideGlideInstance(@ApplicationContext context: Context,  requestOptions: RequestOptions): RequestManager {
            return Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
        }


        @Provides
        @Singleton
        fun provideOkHttp(tokenAuthenticator: TokenAuthenticator): OkHttpClient {
            return OkHttpClient.Builder()
                .authenticator(tokenAuthenticator)
                //.addInterceptor(LoggingInterceptor())
                .build()
        }

        /*
        can not pass TestAPI, it is creating dependency cycle... Dagger is stopping it at compile time....
        @Provides
        @Singleton
        fun provideTokenAuthenticator(testApi: TestAPI, sharedPreferences: SharedPreferences): TokenAuthenticator {
            return TokenAuthenticator(testApi,sharedPreferences)
        }

         */


        @Provides
        @Singleton
        fun provideTokenAuthenticator(sharedPreferences: SharedPreferences): TokenAuthenticator {
            return TokenAuthenticator(sharedPreferences)
        }


        @Provides
        @Singleton
        fun retrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(MyAppConfigConstant.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun appServices(retrofit: Retrofit): TestAPI = retrofit.create(TestAPI::class.java)


        @Provides
        @Singleton
        fun provideLoginDataSource(testAPI: TestAPI): LoginDataSource {
                return LoginDataSource(testAPI)
        }

        @Provides
        @Singleton

        fun provideLoginRepo( dataSource: LoginDataSource,  spEditor: SharedPreferences.Editor , @ApplicationContext applicationContext: Context): LoginRepository {
            return LoginRepository( dataSource,  spEditor, applicationContext )
        }

        @Provides
        @Singleton
        fun provideLoginViewModel( loginRepository: LoginRepository): LoginViewModel {
            return LoginViewModel( loginRepository )
        }

        @Provides
        @Singleton
        fun provideProductRepo( @ApplicationContext applicationContext: Application , loginRepository: LoginRepository): ProductRepository {
            return ProductRepository( applicationContext , loginRepository  )
        }
}