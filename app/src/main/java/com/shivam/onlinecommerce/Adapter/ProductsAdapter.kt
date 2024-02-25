package com.shivam.onlinecommerce.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.DataModel.CategoryProduct
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso

class ProductsAdapter(val context: Context?, val productList: List<CategoryProduct>) : RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {

    private lateinit var mListener : OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_product, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productList[position]


        Picasso.get().load(currentItem.thumbnail).into(holder.image)
        holder.title.text = currentItem.title
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView : View, listener : OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var title: TextView
        init {
            image = itemView.findViewById(R.id.iv_product)
            title = itemView.findViewById(R.id.tv_product_name)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}