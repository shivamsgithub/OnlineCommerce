package com.shivam.onlinecommerce


import retrofit2.Call
import retrofit2.http.GET

interface ProductInterface {

    @GET("products")
    fun getProductsData() : Call<MainProducts>
}