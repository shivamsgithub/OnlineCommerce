package com.shivam.onlinecommerce.Adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.R
import com.squareup.picasso.Picasso

class ImagesViewPagerAdapter(val context: Activity, private var images: List<String>):
    RecyclerView.Adapter<ImagesViewPagerAdapter.PagerViewholder>() {


    private lateinit var onItemClicklisten: OnItemClick

    interface OnItemClick {
        //pass any things
        fun onItemClick(position: Int)

    }

    fun setOnItemClick(onItemClick:OnItemClick) {
            onItemClicklisten = onItemClick

    }
    class PagerViewholder(itemView : View, listener : OnItemClick): RecyclerView.ViewHolder(itemView) {
        val imageProduct : ImageView = itemView.findViewById(R.id.iv_product_images)



        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }


//            imageProduct.setOnClickListener {v: View ->
//                val position : Int = adapterPosition
////                Toast.makeText(itemView.context, "You clicked on item #${position + 1}", Toast.LENGTH_SHORT).show()
//
////                val dialog = Dialog(itemView.context)
////                dialog.setContentView(R.layout.slider_layout)
////                Picasso.get().load(adapterPosition).into(ivFullImage)
////                dialog.show()
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewholder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_image_holder, parent, false)
        return PagerViewholder(item, onItemClicklisten)
    }

    override fun onBindViewHolder(holder: PagerViewholder, position: Int) {


        for (i in 0..images.size){
            val currentitem = images[position]
            Picasso.get().load(currentitem).into(holder.imageProduct)
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClicklisten.onItemClick(position)
        })
    }

    override fun getItemCount(): Int {
        return images.size
    }

}

