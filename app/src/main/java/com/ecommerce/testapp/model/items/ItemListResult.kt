package com.ecommerce.testapp

data class ItemListResult(
    val itemList: List<ItemList>? = null
  )

data class ItemList(
     val id: Int? = null,
     val name: String? = null,
     val description: String? = null,
     val price: Int? = null,
     val itemRate: Int? = null
)