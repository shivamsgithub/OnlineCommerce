package com.shivam.onlinecommerce.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.shivam.onlinecommerce.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//
//        // we used the postDelayed(Runnable, time) method
//        // to send a message with a delayed time.
//        //Normal Handler is deprecated , so we have to change the code little bit
//
//        // Handler().postDelayed({
//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 3000) // 3000 is the delayed time in milliseconds.

        Handler().postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        },1200)
    }
}