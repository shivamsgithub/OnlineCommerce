package com.shivam.onlinecommerce

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    var a = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setBackgroundColor(resources.getColor(R.color.teal_200) )
        toolbar.setTitle("App")
        toolbar.setTitleTextColor(resources.getColor(R.color.black))
        // using toolbar as ActionBar
        setSupportActionBar(toolbar)

        val tvName = findViewById<TextView>(R.id.tv_productName)
        val tvPrice = findViewById<TextView>(R.id.tv_productPrice)
        val tvSpecification = findViewById<TextView>(R.id.tv_productSpecification)
        val btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart)
        var viewPager2  = findViewById<ViewPager2>(R.id.vp_product)
        var ivCart = findViewById<ImageView>(R.id.iv_cart)
        val tvItemCount = findViewById<TextView>(R.id.tv_item_count)
        tvItemCount.setText("0")
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

                tvName.text = responseBody?.title
                tvPrice.text = responseBody?.price.toString()
                tvSpecification.text = responseBody?.description

                btnAddToCart.setOnClickListener{v : View ->
                    Toast.makeText(this@ProductActivity, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                    a++
                    Log.d("count of item", "Item Count: $a")
                    tvItemCount.setText("$a")
                }

                ivCart.setOnClickListener{
                    var intent = Intent (this@ProductActivity , CartActivity::class.java)
                    intent.putExtra("position", productPosition)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: " + t.message)
            }
        })
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
//        var intent = Intent(this@ProductActivity, MainActivity ::class.java)
//        intent.putExtra("count", "$a")
//        startActivity(intent)
//    }

}