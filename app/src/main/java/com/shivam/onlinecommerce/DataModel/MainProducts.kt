package com.shivam.onlinecommerce.DataModel

data class MainProducts(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)