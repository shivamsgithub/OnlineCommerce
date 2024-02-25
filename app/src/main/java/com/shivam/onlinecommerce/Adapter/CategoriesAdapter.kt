package com.shivam.onlinecommerce.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.R

class CategoriesAdapter (val context : Context, val productList: List<String>) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>(){

    private lateinit var mListener : OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.texview_list, parent, false)
        return CategoriesAdapter.MyViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.setText(productList[position])
    }

    class MyViewHolder (itemView : View, listener : OnItemClickListener) :  RecyclerView.ViewHolder(itemView) {

        var title: TextView
        init {
            title = itemView.findViewById(R.id.tv_list)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}