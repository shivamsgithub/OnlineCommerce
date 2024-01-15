package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.viewpager2.widget.ViewPager2
import com.shivam.onlinecommerce.Adapter.ImagesViewPagerAdapter
import com.shivam.onlinecommerce.DataModel.Product
import com.shivam.onlinecommerce.ProductInterface
import com.shivam.onlinecommerce.R
import me.relex.circleindicator.CircleIndicator3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.Base64


class ProductActivity : AppCompatActivity() {

    lateinit var imagesviewAdapter : ImagesViewPagerAdapter
    var a = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.black))

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvName = findViewById<TextView>(R.id.tv_productName)
        val tvPrice = findViewById<TextView>(R.id.tv_productPrice)
        val tvSpecification = findViewById<TextView>(R.id.tv_productSpecification)
        val btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart)
        val btnBuyNow = findViewById<Button>(R.id.btn_buy_now)
        var viewPager2  = findViewById<ViewPager2>(R.id.vp_product)
        var ivCart = findViewById<ImageView>(R.id.iv_cart)
        val tvItemCount = findViewById<TextView>(R.id.tv_item_count_product)
//        btnAddToCart.setText("Add to Cart")


        var productPosition = intent.getIntExtra("productPosition", 0)


        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val productDetails = retrofitBuilder.getProductsDetails(productPosition)

        productDetails.enqueue(object : Callback<Product?> {
            @RequiresApi(Build.VERSION_CODES.O)
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
                tvPrice.text ="₹" + responseBody?.price.toString()
                tvSpecification.text = responseBody?.description

                btnAddToCart.setOnClickListener{v : View ->
                    Toast.makeText(this@ProductActivity, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                    a++
                    Log.d("count of item", "Item Count: $a")
                    tvItemCount.text = "$a"




                    val preferenceManager = getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
                    preferenceManager.putString("name", responseBody.title)
                    preferenceManager.putInt("price", responseBody.price)
                    preferenceManager.putInt("productPosition", productPosition)
                    preferenceManager.putInt("count", a)

                    val bitmap = ivCart.getDrawable().toBitmap()

//                    val bitmap = BitmapFactory.decodeFile(responseBody.thumbnail)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val b = baos.toByteArray()
                    val encodedImage: String = Base64.getEncoder().encodeToString(b)
                    preferenceManager.putString("image_data",encodedImage)
                    Toast.makeText(this@ProductActivity, "Image saved in SharedPreferences", Toast.LENGTH_SHORT).show()
                    preferenceManager.apply()

                }

                ivCart.setOnClickListener{
                    var intent = Intent (this@ProductActivity , CheckOutActivity::class.java)
                    intent.putExtra("position", productPosition)
                    intent.putExtra("count", a)
                    startActivity(intent)
                }

                btnBuyNow.setOnClickListener{
                    var intent = Intent (this@ProductActivity , CheckOutActivity::class.java)
                    intent.putExtra("position", productPosition)
                    intent.putExtra("count", a)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
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


    interface onItemAddedListener{
        fun OnItemAdded (count: Int)
    }

    fun sharedPreferences() {

//        val editor = getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
//        editor.putString("name", "Elena")
//        editor.putInt("idName", 12)
//        editor.apply()
    }
}