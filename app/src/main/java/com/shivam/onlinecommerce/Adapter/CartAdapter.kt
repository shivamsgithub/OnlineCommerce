package com.shivam.onlinecommerce.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.DataModel.Product
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso

class CartAdapter (val context : Activity, val items :  List<Product> ) : RecyclerView.Adapter<CartAdapter.Viewholder>() {

    private lateinit var mListener : onItemAddRemoveListener
    interface onItemAddRemoveListener{
        fun OnItemAddRemove (position: Int)
    }

    fun setOnItemAddRemoveListener (listener: onItemAddRemoveListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_product, parent, false)
        return Viewholder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentitem = items[position]

        holder.title.text = currentitem.title

        // to put image links into  views, we can use picasso library
        Picasso.get().load(currentitem.thumbnail).into(holder.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class Viewholder (itemView : View, mlistener : onItemAddRemoveListener) : RecyclerView.ViewHolder(itemView){

        var image: ImageView
        var title: TextView

        init {
            image = itemView.findViewById(R.id.iv_product)
            title = itemView.findViewById(R.id.tv_product_name)

            itemView.setOnClickListener {
                mlistener.OnItemAddRemove(adapterPosition)
            }
        }
    }
}