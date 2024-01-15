package com.shivam.onlinecommerce.Adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso

class ImagesViewPagerAdapter(val context: Activity, private var images: List<String>):
    RecyclerView.Adapter<ImagesViewPagerAdapter.PagerViewholder>() {

    class PagerViewholder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val imageProduct : ImageView = itemView.findViewById(R.id.iv_product_images)

        init {
            imageProduct.setOnClickListener {v: View ->
                val position : Int = adapterPosition
                Toast.makeText(itemView.context, "You clicked on item #${position + 1}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewholder {
        return PagerViewholder(LayoutInflater.from(parent.context).inflate(R.layout.product_image_holder, parent, false))
    }

    override fun onBindViewHolder(holder: PagerViewholder, position: Int) {


        for (i in 0..images.size){
            val currentitem = images[position]
//            holder.imageProduct.setImageResource(currentitem.images.get(i))

            Picasso.get().load(currentitem).into(holder.imageProduct)

        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}

