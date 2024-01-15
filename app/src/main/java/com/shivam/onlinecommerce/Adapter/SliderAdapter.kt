package com.shivam.onlinecommerce.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.shivam.onlinecommerce.R
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso


class SliderAdapter(SliderDataModelArrayList: ArrayList<String>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder?>() {
    // list for storing urls of images.
    private val mSliderItems: List<String>

    // Constructor
    init {
        mSliderItems = SliderDataModelArrayList
    }

     // We are inflating t slider_layout
    // inside on Create View Holder method.
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterViewHolder {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, null)
        return SliderAdapterViewHolder(inflate)
    }

     // Inside on bind view holder we will
    // set data to item of Slider View.
     override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder?, position: Int) {
        val sliderItem: String = mSliderItems[position]

        // Glide is use to load image
        // from url in your imageview.

//         if (viewHolder != null) {
//             Glide.with(viewHolder.itemView)
//                 .load(sliderItem.getImgUrl())
//                 .fitCenter()
//                 .into(viewHolder.imageViewBackground)
//         }

         if (viewHolder != null) {
             Picasso.get().load(sliderItem).into(viewHolder.imageViewBackground)
         }

    }

     override fun getCount(): Int {
         return mSliderItems.size
     }

    class SliderAdapterViewHolder(// Adapter class for initializing
        // the views of our slider view.
        var itemView: View
    ) : SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById<ImageView>(R.id.myimage)
        }
    }
}