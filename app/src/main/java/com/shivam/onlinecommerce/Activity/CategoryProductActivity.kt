package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.Adapter.CategoriesAdapter
import com.shivam.onlinecommerce.Adapter.ProductsAdapter
import com.shivam.onlinecommerce.DataModel.CategoryProductsListModel
import com.shivam.onlinecommerce.ProductInterface
import com.shivam.onlinecommerce.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryProductActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    //    var activity: Activity? = getActivity()
    lateinit var productsAdapter : ProductsAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_product)

        val toolbar = findViewById<View>(R.id.
        toolbar_cate_pro) as androidx.appcompat.widget.Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.rv_products)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val productData = retrofitBuilder.getCategoryProduct()




        productData.enqueue(object : Callback<CategoryProductsListModel> {
            override fun onResponse(call: Call<CategoryProductsListModel>, response: Response<CategoryProductsListModel>) {
                var responseBody = response.body()
                val productArray = responseBody?.products

                    productsAdapter =
                        productArray?.let { ProductsAdapter(this@CategoryProductActivity, it) }!!
                    recyclerView.adapter = productsAdapter
                    recyclerView.layoutManager = LinearLayoutManager(this@CategoryProductActivity)


                productsAdapter.setOnItemClickListener(object : ProductsAdapter.OnItemClickListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onItemClick(position: Int) {
                        val position = position + 1
//                            Toast.makeText(this@MainActivity, "you clicked on item no. $position", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CategoryProductActivity, ProductActivity::class.java)
                        intent.putExtra("productPosition", position)
                        startActivity(intent)

                    }
                })

            }

            override fun onFailure(call: Call<CategoryProductsListModel>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: " + t.message)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
