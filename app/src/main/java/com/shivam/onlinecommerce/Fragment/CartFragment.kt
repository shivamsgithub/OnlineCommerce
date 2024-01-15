package com.shivam.onlinecommerce.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.shivam.onlinecommerce.Activity.CheckOutActivity
import com.shivam.onlinecommerce.R
import java.util.Base64


class CartFragment : Fragment() {

// lateinit var adapter : CartAdapter

    var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv_name = view.findViewById<TextView>(R.id.tv_name)
        val tv_price = view.findViewById<TextView>(R.id.tv_price)
        val iv_image = view.findViewById<ImageView>(R.id.iv_cart_fragment)

        val btnBuyNow = view.findViewById<Button>(R.id.btn_buy_now_cart)

        val prefs: SharedPreferences = requireContext().getSharedPreferences("Cart Values", MODE_PRIVATE)
        val name = prefs.getString("name", "No Name defined")
        val price = prefs.getInt("price", 0)
        val productPosition = prefs.getInt("productPosition", 0)
        val count = prefs.getInt("count", 0)


//        val preferenceManager = PreferenceManager.getDefaultSharedPreferencesName(context)

        preferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


//        String encodedSaveImage = preferences.getString("image","");
//        byte[] decodedString = Base64.decode(encodedSaveImage,Base64.DEFAULT);
//        Bitmap decodeBitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
//        if (decodeBitmap != null){
//            saveImage.setImageBitmap(decodeBitmap);




            val previouslyEncodedImage: String? = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("image_data", "")

            val b: ByteArray = Base64.getDecoder().decode(previouslyEncodedImage)

        val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)


        tv_name.setText("$name")
        tv_price.setText("$price")
        if (bitmap != null){
            iv_image.setImageBitmap(bitmap)
        }else {
            iv_image.setImageResource(R.drawable.ic_launcher_background)
        }

        btnBuyNow.setOnClickListener{
            var intent = Intent (requireContext() , CheckOutActivity::class.java)
            intent.putExtra("position", productPosition)
            intent.putExtra("count", count)
            startActivity(intent)
        }

    }
}