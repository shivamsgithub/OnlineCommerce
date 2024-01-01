package com.shivam.onlinecommerce

data class MainProducts(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)