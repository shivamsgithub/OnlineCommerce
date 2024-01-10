package com.shivam.onlinecommerce

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.squareup.picasso.Picasso
import me.relex.circleindicator.CircleIndicator3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var position = intent.getIntExtra("position", 0 )

        var tvPrice = findViewById<TextView>(R.id.tv_productPrice_cart)
        var tvName = findViewById<TextView>(R.id.tv_productName_cart)
        var ivImage = findViewById<ImageView>(R.id.iv_product_cart)

//        742

        val btnProceedPayment = findViewById<Button>(R.id.btn_proceed_payment)
        btnProceedPayment.setOnClickListener{v : View ->
            var intent = Intent (this@CartActivity, PaymentActivity ::class.java)
            intent.putExtra("price", position)
            startActivity(intent)
        }


        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val productDetails = retrofitBuilder.getProductsDetails(position)

        productDetails.enqueue(object : Callback<Product?> {
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    var priceItem = responseBody.price
                    tvPrice.setText("$priceItem")
                }
                if (responseBody != null) {
                    var nameItem = responseBody.title
                    tvName.setText("$nameItem")
                }

                if (responseBody != null) {
                    Picasso.get().load(responseBody.thumbnail).into(ivImage)
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: " + t.message)
            }
        })

    }
}