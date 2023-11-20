package com.ecommerce.testapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ItemListingRepo @Inject constructor(private val dataSource: ProductsDataSource)
    {
    // Token can be sent from here in the function parameter but I will grab that in DataSource....
    //suspend fun getAllProducts(): ApiResult<AllProductResponse> = dataSource.getAllProducts()



    fun getAllProducts(): Flow<List<Product>> = flow {
        emit(dataSource.getAllProducts())
    }.flowOn(Dispatchers.IO)
}