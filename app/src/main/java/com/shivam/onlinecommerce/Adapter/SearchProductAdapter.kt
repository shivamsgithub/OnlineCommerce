package com.shivam.onlinecommerce.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.DataModel.ProductX
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso

class SearchProductAdapter (val context : Activity ,val searchList:List<ProductX>):RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    interface onItemClickListener{
        fun OnSearchItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_product, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = searchList[position]

        holder.title.text = currentitem.title

        // to put image links into  views, we can use picasso library
        Picasso.get().load(currentitem.thumbnail).into(holder.image)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class ViewHolder (itemView : View, listener : onItemClickListener) : RecyclerView.ViewHolder(itemView){

        var image: ImageView
        var title: TextView
        init {
            image = itemView.findViewById(R.id.iv_product)
            title = itemView.findViewById(R.id.tv_product_name)
            itemView.setOnClickListener {
                listener.OnSearchItemClick(adapterPosition)
            }
        }
    }


}