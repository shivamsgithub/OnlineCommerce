package com.shivam.onlinecommerce

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.SliderView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var mainAdapter: MainAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.black))
        // using toolbar as ActionBar
        setSupportActionBar(toolbar)

        val searchbox : SearchView = findViewById(R.id.sv_products)

        var ivCart = findViewById<ImageView>(R.id.iv_cart)
        var tvCartCount = findViewById<TextView>(R.id.tv_item_count_main)

        recyclerView = findViewById<RecyclerView>(R.id.rv_products)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val productData = retrofitBuilder.getProductsData()

        productData.enqueue(object : Callback<MainProducts?> {
            override fun onResponse(call: Call<MainProducts?>, response: Response<MainProducts?>) {
                var responseBody = response.body()
                val productArray = responseBody?.products!!

                mainAdapter = MainAdapter(this@MainActivity, productArray)
                recyclerView.adapter = mainAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                var url1 = "https://images.gizbot.com/webp/img/2019/10/flipkart-big-billion-days-sale-upto-50-discount-offers-on-t" +
                        "ablets-1570089579.jpg"
                var url2 = "https://img.global.news.samsung.com/in/wp-content/uploads/2022/03/Blue-fest-3000x2000px.jpg"
                var url3 = "https://pbs.twimg.com/media/FEi5zXOVIAIHCcd.jpg:large"
                val SliderDataModelArrayList: ArrayList<String> = ArrayList()
                SliderDataModelArrayList.add(url1)
                SliderDataModelArrayList.add(url2)
                SliderDataModelArrayList.add(url3)

                val adapter = SliderAdapter(this@MainActivity, SliderDataModelArrayList)
                val sliderView = findViewById<SliderView>(R.id.slider)
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR)
                sliderView.setSliderAdapter(adapter)
                sliderView.setScrollTimeInSec(3)
                sliderView.setAutoCycle(true)
                sliderView.startAutoCycle()




                mainAdapter.setOnItemClickListener(object : MainAdapter.onItemClickListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun OnItemClick(position: Int) {
                        val position = position + 1
//                            Toast.makeText(this@MainActivity, "you clicked on item no. $position", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, ProductActivity::class.java)
                        intent.putExtra("productPosition", position)
                        startActivity(intent)

                        var cartCount = intent.getIntExtra("count", 0)
                        tvCartCount.setText("$cartCount")

                    }
                })

                ivCart.setOnClickListener{
                    var intent = Intent (this@MainActivity , CartActivity::class.java)
                    startActivity(intent)
                }

            }

            override fun onFailure(call: Call<MainProducts?>, t: Throwable) {
                Log.d(TAG, "onFailure: " + t.message)
            }
        })
    }
}