package com.shivam.onlinecommerce

data class SearchProductModel(
    val limit: Int,
    val products: List<ProductX>,
    val skip: Int,
    val total: Int
)