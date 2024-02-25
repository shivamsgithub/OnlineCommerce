package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import com.phonepe.intent.sdk.api.UPIApplicationInfo
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment
import com.shivam.onlinecommerce.DataModel.Product
import com.shivam.onlinecommerce.ProductInterface
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.security.MessageDigest

class CheckOutActivity : AppCompatActivity() {

    lateinit var tvPrice : TextView
    lateinit var tvName : TextView
    lateinit var ivImage : ImageView
    var position : Int = 0
    var count : Int = 0
    var buyCount : Int = 0

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        position = intent.getIntExtra("position", 0 )
        count = intent.getIntExtra("count", 0)
        buyCount = intent.getIntExtra("buyCount", 0)
        tvPrice = findViewById(R.id.tv_productPrice_cart)
        tvName = findViewById<TextView>(R.id.tv_productName_cart)
        ivImage = findViewById<ImageView>(R.id.iv_product_cart)
        var btnAddUnit = findViewById<Button>(R.id.btn_add_unit)
        var btnRemoveUnit = findViewById<Button>(R.id.btn_remove_unit)
        var tvTotalUnit = findViewById<TextView>(R.id.tv_total_unit)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvTotalUnit.setText("$count")

        checkOutAPI(count)

        btnAddUnit.setOnClickListener{
            btnRemoveUnit.setBackgroundColor(Color.parseColor("#BDBABA"))
            btnRemoveUnit.isClickable = true
            count += 1
            tvTotalUnit.setText("$count")
            checkOutAPI(count)

            val preferenceManager = getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
            preferenceManager.putInt("count", count)
            preferenceManager.apply()
        }

        btnRemoveUnit.setOnClickListener{
            count -= 1    //count = count-1
            val preferenceManager = getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
            preferenceManager.putInt("count", count)
            preferenceManager.apply()

            if(count <= 0){
                var intent = Intent (this@CheckOutActivity, MainActivity:: class.java)
                startActivity(intent)
            } else if (count == 1 ){
                tvTotalUnit.setText("$count")
                btnRemoveUnit.setBackgroundColor(Color.GRAY)
                checkOutAPI(1)
                btnRemoveUnit.isClickable = false
            }else {
                btnRemoveUnit.setBackgroundColor(Color.parseColor("#BDBABA"))
                btnRemoveUnit.isClickable = true
                tvTotalUnit.setText("$count")
                checkOutAPI(count)
            }
        }
    }

    fun checkOutAPI (countNew : Int){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val prefs: SharedPreferences = getSharedPreferences("Cart Values", MODE_PRIVATE)
        var id = prefs.getInt("productId", 0)

        val productDetails = retrofitBuilder.getProductsDetails(id)

        productDetails.enqueue(object : Callback<Product?> {
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    var priceItem = responseBody.price
                    var totalPrice: Int = priceItem * countNew
                    tvPrice.setText("₹" + "$totalPrice")

                    var nameItem = responseBody.title
                    tvName.setText("$nameItem")

                    Picasso.get().load(responseBody.thumbnail).into(ivImage)

                    if (countNew > 0){
                        phonepeCode(totalPrice)
                    } else{
                        Toast.makeText(this@CheckOutActivity, "Total unit cannot be less than 1", Toast.LENGTH_SHORT).show()
                    }
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
        data.put ("amount", price*100 )
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

            Toast.makeText(this, "check callback url. Result Code is : $resultCode", Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            finish()
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