package com.shivam.onlinecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        var price = intent.getIntExtra("price", 0 )
    }
}