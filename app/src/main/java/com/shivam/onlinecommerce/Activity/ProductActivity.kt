package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.core.view.get
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
    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private lateinit var mImageView: ImageView
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
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)

        btnAddToCart.setText("Add to Cart")

        var productPosition = intent.getIntExtra("productPosition", 0)

        val prefs: SharedPreferences = getSharedPreferences("Cart Values", MODE_PRIVATE)
        var count = prefs.getInt("count", 0)
        tvItemCount.setText("$count")


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

                imagesviewAdapter.setOnItemClick(object : ImagesViewPagerAdapter.OnItemClick {
                    override fun onItemClick(position: Int) {
//                        var imge  = imageArray.get(productPosition).get(0).digitToInt()
//                        var num  = imge.(Character::getNumericValue)
                        val myUri = Uri.parse(imageArray.get(productPosition)[0].toString())
                        dialogBox(myUri)

                    }
                })

                val indicator : CircleIndicator3 = findViewById<CircleIndicator3>(R.id.indicator)
                indicator.setViewPager(viewPager2)
                tvName.text = responseBody?.title
                tvPrice.text ="₹" + responseBody?.price.toString()
                tvSpecification.text = responseBody?.description
                ratingBar.rating = responseBody.rating.toFloat()

                btnAddToCart.setOnClickListener{v : View ->

                    if (btnAddToCart.text == "Go To Cart") {
                        val i = Intent(this@ProductActivity, MainActivity::class.java)
                        i.putExtra("fragment", 2)
                        startActivity(i)
                    }

                    count++
                    tvItemCount.text = "$count"

                    val preferenceManager = getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
                    preferenceManager.putString("name", responseBody.title)
                    preferenceManager.putInt("price", responseBody.price)
                    preferenceManager.putInt("productId", responseBody.id)
                    preferenceManager.putInt("count", count)

                    val bitmap = viewPager2.get(0).drawToBitmap()
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val b = baos.toByteArray()
                    val encodedImage: String = Base64.getEncoder().encodeToString(b)
                    preferenceManager.putString("image_data",encodedImage)
//                    Toast.makeText(this@ProductActivity, "Image saved in SharedPreferences", Toast.LENGTH_SHORT).show()
                    preferenceManager.apply()

                    btnAddToCart.text = "Go To Cart"

                }

                ivCart.setOnClickListener{
                    var intent = Intent (this@ProductActivity , CheckOutActivity::class.java)
                    intent.putExtra("position", productPosition)
                    intent.putExtra("count", count)
                    startActivity(intent)
                }

                btnBuyNow.setOnClickListener{

                    val preferenceManager = getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
                    preferenceManager.putString("name", responseBody.title)
                    preferenceManager.putInt("price", responseBody.price)
                    preferenceManager.putInt("productId", responseBody.id)
                    preferenceManager.putInt("count", 1)

                    val bitmap = viewPager2.get(0).drawToBitmap()
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val b = baos.toByteArray()
                    val encodedImage: String = Base64.getEncoder().encodeToString(b)
                    preferenceManager.putString("image_data",encodedImage)
//                    Toast.makeText(this@ProductActivity, "Image saved in SharedPreferences", Toast.LENGTH_SHORT).show()
                    preferenceManager.apply()





                    var intent = Intent (this@ProductActivity , CheckOutActivity::class.java)
                    intent.putExtra("position", productPosition)
                    intent.putExtra("buyCount", 1)
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

    @SuppressLint("SuspiciousIndentation")
    fun dialogBox(img: Uri) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.full_image)
        dialog.setTitle("Dialog Box")
        val image = dialog.findViewById<ImageView>(R.id.myimage)


            image.setImageURI(img)

            image.setOnClickListener{
                image.setScaleX(2.0f)
                image.setScaleY(2.0f)
            }
        val closeDialog = dialog.findViewById<ImageView>(R.id.iv_close_dialog)
        closeDialog.setOnClickListener {
            dialog.dismiss() }
        dialog.show()

//        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return mScaleGestureDetector.onTouchEvent(event)
//    }


//    private class ScaleListener : SimpleOnScaleGestureListener() {
//        // when a scale gesture is detected, use it to resize the image
//
//        private var mScaleFactor = 1.0f
//        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
//            mScaleFactor = scaleGestureDetector.scaleFactor
//
//            return true
//        }
//    }


}