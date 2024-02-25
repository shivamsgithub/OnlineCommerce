package com.shivam.onlinecommerce.Fragment

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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.shivam.onlinecommerce.Activity.CheckOutActivity
import com.shivam.onlinecommerce.Adapter.CartAdapter
import com.shivam.onlinecommerce.R
import java.util.Base64

class CartFragment : Fragment() {

    lateinit var adapter : CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv_name = view.findViewById<TextView>(R.id.tv_name_cart_item)
        val tv_price = view.findViewById<TextView>(R.id.tv_price_cart_item)
        val iv_image = view.findViewById<ImageView>(R.id.iv_cart_item)
        val tv_empty_msg = view.findViewById<TextView>(R.id.tv_empty_msg)
        val rlCartItems = view.findViewById<RelativeLayout>(R.id.rl_cart_item_bg)
        val btnRemoveFromCart = view.findViewById<Button>(R.id.btn_remove_from_cart)
        val btnAddToCart = view.findViewById<Button>(R.id.btn_add_to_cart)
        val tvNoOFItem = view.findViewById<TextView>(R.id.tv_cart_count)

        rlCartItems.visibility = View.GONE
        btnRemoveFromCart.visibility = View.GONE
        btnAddToCart.visibility = View.GONE
        tvNoOFItem.visibility = View.GONE
        tv_empty_msg.visibility = View.VISIBLE


        val btnBuyNow = view.findViewById<Button>(R.id.btn_buy_now_cart)
//        val rvcartitems = view.findViewById<RecyclerView>(R.id.rv_cart_items)

        val prefs: SharedPreferences = requireContext().getSharedPreferences("Cart Values", MODE_PRIVATE)
        val name = prefs.getString("name", "")
        val price = prefs.getInt("price", 0)
        val productPosition = prefs.getInt("productId", 0)
        var count = prefs.getInt("count", 0)

        val previouslyEncodedImage: String? = requireContext().getSharedPreferences("Cart Values", Context.MODE_PRIVATE).getString("image_data", "")

        val b: ByteArray = Base64.getDecoder().decode(previouslyEncodedImage)

        val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)

        tv_name.setText("$name")
        tv_price.setText("$price")
        tvNoOFItem.setText("$count")
        if (bitmap != null){
            iv_image.setImageBitmap(bitmap)
        }else {
            iv_image.setImageResource(R.drawable.ic_launcher_background)
        }

        if (name != null && name != ""){
            rlCartItems.visibility = View.VISIBLE
            btnRemoveFromCart.visibility = View.VISIBLE
            btnAddToCart.visibility = View.VISIBLE
            tvNoOFItem.visibility = View.VISIBLE
            tv_empty_msg.visibility = View.GONE
        } else {
            rlCartItems.visibility = View.GONE
            btnRemoveFromCart.visibility = View.GONE
            btnAddToCart.visibility = View.GONE
            tvNoOFItem.visibility = View.GONE
            tv_empty_msg.visibility = View.VISIBLE
        }

        btnRemoveFromCart.setOnClickListener{

            count -= 1
            val preferenceManager = requireContext().getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
            preferenceManager.putInt("count", count)
            preferenceManager.apply()

            if(count < 1) {
                prefs.edit().clear().apply()

                Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
                rlCartItems.visibility = View.GONE
                btnRemoveFromCart.visibility = View.GONE
                btnAddToCart.visibility = View.GONE
                tvNoOFItem.visibility = View.GONE
                tv_empty_msg.visibility = View.VISIBLE
            } else {
               tvNoOFItem.setText("$count")
                rlCartItems.visibility = View.VISIBLE
                btnRemoveFromCart.visibility = View.VISIBLE
                btnAddToCart.visibility = View.VISIBLE
                tvNoOFItem.visibility = View.VISIBLE
                tv_empty_msg.visibility = View.GONE
            }
        }

        btnAddToCart.setOnClickListener{

            count += 1
            tvNoOFItem.setText("$count")
            Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()

            val preferenceManager = requireContext().getSharedPreferences("Cart Values", MODE_PRIVATE).edit()
            preferenceManager.putInt("count", count)
            preferenceManager.apply()

            rlCartItems.visibility = View.VISIBLE
            btnRemoveFromCart.visibility = View.VISIBLE
            btnAddToCart.visibility = View.VISIBLE
            tvNoOFItem.visibility = View.VISIBLE
            tv_empty_msg.visibility = View.GONE

        }

        btnBuyNow.setOnClickListener{
            var intent = Intent (requireContext() , CheckOutActivity::class.java)
            intent.putExtra("position", productPosition)
            intent.putExtra("count", count)
            startActivity(intent)
        }
    }
}