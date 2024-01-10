package com.shivam.onlinecommerce

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductActivity : AppCompatActivity() {

    lateinit var imagesviewAdapter : ImagesViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val tvName = findViewById<TextView>(R.id.tv_productName)
        val tvPrice = findViewById<TextView>(R.id.tv_productPrice)
        val tvSpecification = findViewById<TextView>(R.id.tv_productSpecification)
        val btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart)
        var viewPager2  = findViewById<ViewPager2>(R.id.vp_product)
        btnAddToCart.setText("Add to Cart")

        var productPosition = intent.getIntExtra("productPosition", 0)


        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val productDetails = retrofitBuilder.getProductsDetails(productPosition)

        productDetails.enqueue(object : Callback<Product?> {
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                val responseBody = response.body()
                val imageArray = responseBody?.images!!
//                val imagearrayInt = imageArray.map { it.toInt() }.toTypedArray()

                imagesviewAdapter = ImagesViewPagerAdapter(this@ProductActivity, imageArray)
                viewPager2.adapter = imagesviewAdapter
                viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                val indicator : CircleIndicator3 = findViewById<CircleIndicator3>(R.id.indicator)
                indicator.setViewPager(viewPager2)

//                viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//                    override fun onPageScrolled(
//                        position: Int,
//                        positionOffset: Float,
//                        positionOffsetPixels: Int
//                    ) {
//                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                    }
//                    override fun onPageSelected(position: Int) {
//                        super.onPageSelected(position)
//                    }
//                    override fun onPageScrollStateChanged(state: Int) {
//                        super.onPageScrollStateChanged(state)
//                    }
//                })

                tvName.text = responseBody?.title
                tvPrice.text = responseBody?.price.toString()
                tvSpecification.text = responseBody?.description

            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: " + t.message)
            }
        })

    }

}