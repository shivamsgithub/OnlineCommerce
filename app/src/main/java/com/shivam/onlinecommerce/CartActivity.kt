package com.shivam.onlinecommerce

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import com.phonepe.intent.sdk.api.UPIApplicationInfo
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment
import com.squareup.picasso.Picasso
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.security.MessageDigest

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var position = intent.getIntExtra("position", 0 )
        var count = intent.getIntExtra("count", 0)
        var tvPrice = findViewById<TextView>(R.id.tv_productPrice_cart)
        var tvName = findViewById<TextView>(R.id.tv_productName_cart)
        var ivImage = findViewById<ImageView>(R.id.iv_product_cart)
        var tvItemCount = findViewById<TextView>(R.id.tv_item_count)

        tvItemCount.text = "$count"

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



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
                    tvPrice.setText("₹" + "$priceItem")

                    phonepeCode(priceItem)

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

    fun phonepeCode (price : Int){
        PhonePe.init(this, PhonePeEnvironment.STAGE, "PGTESTPAYUAT", "REPLACE_WITH_YOUR_APP_ID")

        try {
            PhonePe.setFlowId("Unique Id of the user") // Recommended, not mandatory , An alphanumeric string without any special character
            var upiApps:List<UPIApplicationInfo>  = PhonePe.getUpiApps()
        } catch (exception : PhonePeInitException) {
            exception.printStackTrace();
        }

//        val data : HashMap<String?, Any?> = hashMapOf()
        val data = JSONObject()

        data.put ("merchantId","PGTESTPAYUAT")
        data.put ("merchantTransactionId", System.currentTimeMillis().toString())
        data.put ("merchantUserId", System.currentTimeMillis().toString())
        Log.d("Price", " " + price )
        data.put ("amount", price )
        data.put("mobileNumber", "9999999999")
        data.put ("callbackUrl", "https://webhook.site/260afded-143c-4962-a0e7-30cb8cd6d0da")

        val mpaymentInstrument = JSONObject()
        mpaymentInstrument.put("type","PAY_PAGE")
        data.put ("paymentInstrument", mpaymentInstrument)
        val base64Body : String = Base64.encodeToString(data.toString().toByteArray(
            Charset.defaultCharset()
        ), Base64.NO_WRAP)

        val checksum : String = sha256(base64Body + "/pg/v1/pay" + "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399") + "###1"

        val b2BPGRequest = B2BPGRequestBuilder()
            .setData(base64Body)
            .setChecksum(checksum)
            .setUrl("/pg/v1/pay")
            .build()

        val btnProceedPayment = findViewById<Button>(R.id.btn_proceed_payment)
        btnProceedPayment.setOnClickListener{v : View ->

            try {
                startActivityForResult(
                    PhonePe.getImplicitIntent(
                        this, b2BPGRequest, ""),1);
            } catch( e: Exception){
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {

            Toast.makeText(this, "check callback url", Toast.LENGTH_SHORT).show()
        }
    }

    private  fun  sha256(input:String) : String{
        val bytes : ByteArray = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest: ByteArray = md.digest(bytes)
        return  digest.fold(""){str, it -> str + "%02x".format(it)}

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