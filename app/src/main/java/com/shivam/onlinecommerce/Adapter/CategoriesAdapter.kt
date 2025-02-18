package com.shivam.onlinecommerce.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso

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
//        holder.title.setText(productList[position])



        val imageMap = mapOf(

            "electronics" to R.drawable.electronic,
            "jewelery" to R.drawable.jewellery,
            "men's clothing" to R.drawable.men_clothin,
            "women's clothing" to R.drawable.womens_clothing
        )
//        Picasso.get().load((productList[position]).th).into(holder.image)

        val productName = productList[position]
        holder.title.text = productName

        // Get the corresponding image, if not found use a default image
        val imageRes = imageMap[productName] ?: R.drawable.electronic
        Picasso.get()
            .load(imageRes)
            .resize(200, 200)
            .centerCrop()
            .into(holder.image);

    }

    class MyViewHolder (itemView : View, listener : OnItemClickListener) :  RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var image : ImageView
        init {
            title = itemView.findViewById(R.id.tv_list)
            image = itemView.findViewById(R.id.iv_list_categories)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}