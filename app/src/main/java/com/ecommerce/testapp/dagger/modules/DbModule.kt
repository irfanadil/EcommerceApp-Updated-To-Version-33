package com.ecommerce.testapp.dagger.modules

import android.content.Context
import androidx.room.Room
import com.ecommerce.testapp.AppConstant
import com.ecommerce.testapp.CartItemRepo
import com.ecommerce.testapp.CartItemsDao
import com.ecommerce.testapp.EcommerceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, EcommerceDatabase::class.java, AppConstant.DATABASE_NAME)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: EcommerceDatabase) = db.cartItemsDao()

    //@Provides
    //fun provideEntity() = CartItem()

    @Provides
    @Singleton
    fun provideCartItemsDao( cartItemsDao: CartItemsDao): CartItemRepo {
        return CartItemRepo(cartItemsDao)
    }


}

