package com.shivam.onlinecommerce


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ProductInterface {

    @GET("products")
    fun getProductsData() : Call<MainProducts>

    @GET("products/{id}")
//    fun getProductsDetails () : Call<Product>

    fun getProductsDetails(@Path("id") id: Int): Call<Product>
}