package com.shivam.onlinecommerce

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MainAdapter(val context: Activity, val productList: List<Product>) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener{
        fun OnItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    // Layout Manager fails to create view for some data then this method is used
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_product, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    // populate data in the view
    override fun onBindViewHolder(holder: MainAdapter.MyViewHolder, position: Int) {
        val currentitem = productList[position]

        holder.title.text = currentitem.title

        // to put image links into  views, we can use picasso library
        Picasso.get().load(currentitem.thumbnail).into(holder.image)
    }


    // returns the size of the list
    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView : View, listener : onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var title: TextView

        init {
            image = itemView.findViewById(R.id.iv_product)
            title = itemView.findViewById(R.id.tv_product_name)

            itemView.setOnClickListener {
                listener.OnItemClick(adapterPosition)
            }
        }
    }

}