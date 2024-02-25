package com.shivam.onlinecommerce.DataModel

class CategoryProductsListModel (

    val limit: Int,
    val products: List<CategoryProduct>,
    val skip: Int,
    val total: Int

)