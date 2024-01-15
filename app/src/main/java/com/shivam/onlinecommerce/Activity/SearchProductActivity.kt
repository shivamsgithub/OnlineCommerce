package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.ProductInterface
import com.shivam.onlinecommerce.R
import com.shivam.onlinecommerce.Adapter.SearchProductAdapter
import com.shivam.onlinecommerce.DataModel.SearchProductModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchProductActivity : AppCompatActivity() {

    lateinit var searchAdapter : SearchProductAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        recyclerView = findViewById(R.id.rv_search_products)
        var searchQuery = intent.getStringExtra("searchInput")

        val retrofitBuilder2 = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val searchedProductData = searchQuery?.let { retrofitBuilder2.getSearchedProducts(searchQuery) }

        searchedProductData?.enqueue(object : Callback<SearchProductModel?> {
            override fun onResponse(call: Call<SearchProductModel?>, response: Response<SearchProductModel?>) {
                var response = response.body()
                var searchProductArray = response?.products!!

                searchAdapter = SearchProductAdapter(this@SearchProductActivity, searchProductArray)
                recyclerView.adapter = searchAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@SearchProductActivity)

                searchAdapter.setOnItemClickListener(object :
                    SearchProductAdapter.onItemClickListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun OnSearchItemClick(position: Int) {
//                        val position = position + 1
//                        val intent = Intent(this@SearchProductActivity, ProductActivity::class.java)
//                        intent.putExtra("productPosition", position)
//                        startActivity(intent)
                    }
                })
            }

            override fun onFailure(call: Call<SearchProductModel?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: " + t.message)
            }
        })
    }
}