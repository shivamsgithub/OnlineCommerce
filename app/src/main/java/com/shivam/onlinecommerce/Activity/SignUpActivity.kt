package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.shivam.onlinecommerce.R

class SignUpActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var tvsignIn = findViewById<TextView>(R.id.tv_sign_in)
        tvsignIn.setOnClickListener{
            intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        var btnSkiptoMain = findViewById<Button>(R.id.btn_skip_to_main_activity_from_sign_up)
        btnSkiptoMain.setOnClickListener{
            intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}