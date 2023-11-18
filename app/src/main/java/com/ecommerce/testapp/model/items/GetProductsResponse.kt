package com.ecommerce.testapp

data class AllProductResponse(
    val products: List<Product>? = null
  )

data class Product(
    val id: Int? = null,
    val title: String? = null,
    val category: String? = null,
    val price: Double? = null,
    val rating: Rating,
    val description: String? = null,
    val image: String? = null
)

data class Rating(
    val count: Int,
    val rate: Double
)

