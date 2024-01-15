package com.shivam.onlinecommerce.DataModel

data class SearchProductModel(
    val limit: Int,
    val products: List<ProductX>,
    val skip: Int,
    val total: Int
)