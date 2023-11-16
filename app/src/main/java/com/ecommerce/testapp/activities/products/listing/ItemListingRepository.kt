package com.ecommerce.testapp

import javax.inject.Inject

class ItemListingRepository @Inject constructor(private val dataSource: ItemListingDataSource)
    {
    // Token can be sent from here in the function parameter but I will grab that in DataSource....
    suspend fun fetchAllItems(): Result<ItemListResult> =
        dataSource.fetchAllItems()
}