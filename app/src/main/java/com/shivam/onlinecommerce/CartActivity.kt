package com.shivam.onlinecommerce

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var price = intent.getIntExtra("price", 0 )

        var tvPrice = findViewById<TextView>(R.id.tv_productPrice_cart)
        tvPrice.setText("$price")

        val btnProceedPayment = findViewById<Button>(R.id.btn_proceed_payment)
        btnProceedPayment.setOnClickListener{v : View ->
            var intent = Intent (this@CartActivity, PaymentActivity ::class.java)
            intent.putExtra("price", price)
            startActivity(intent)
        }

    }
}