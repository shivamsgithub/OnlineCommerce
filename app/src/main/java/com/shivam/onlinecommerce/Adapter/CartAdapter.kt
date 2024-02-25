package com.shivam.onlinecommerce.Adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.DataModel.Product
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso

class CartAdapter(val context: Activity, val items: List<Product>) : RecyclerView.Adapter<CartAdapter.Viewholder>() {

    private lateinit var mListener : onItemAddRemoveListener
    var preferences: SharedPreferences? = null
    interface onItemAddRemoveListener{
        fun OnItemAddRemove (position: Int)
    }

    fun setOnItemAddRemoveListener (listener: onItemAddRemoveListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false)
        return Viewholder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentitem = items[position]


        val prefs: SharedPreferences = context.getSharedPreferences("Cart Values", Context.MODE_PRIVATE)
//        val name = prefs.getString("name", "No Name defined")
//        val price = prefs.getInt("price", 0)
        val productsInCart = prefs.getInt("productId", 0)



        for ( i in 0.. items.size){
            if (i == productsInCart){
                holder.title.text = currentitem.title
                holder.price.text = currentitem.price.toString()
                Picasso.get().load(currentitem.thumbnail).into(holder.image)
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    class Viewholder (itemView : View, mlistener : onItemAddRemoveListener) : RecyclerView.ViewHolder(itemView){

        var image: ImageView
        var title: TextView
        var price: TextView

        init {
            image = itemView.findViewById(R.id.iv_cart_item)
            title = itemView.findViewById(R.id.tv_name_cart_item)
            price = itemView.findViewById(R.id.tv_price_cart_item)

            image.setOnClickListener {
                mlistener.OnItemAddRemove(adapterPosition)
            }

        }
    }
}