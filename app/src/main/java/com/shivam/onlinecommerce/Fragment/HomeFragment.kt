package com.shivam.onlinecommerce.Fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivam.onlinecommerce.Activity.CategoryProductActivity
import com.shivam.onlinecommerce.Activity.SearchProductActivity
import com.shivam.onlinecommerce.Adapter.CategoriesAdapter
//import com.shivam.onlinecommerce.Adapter.SliderAdapter
import com.shivam.onlinecommerce.DataModel.CategoriesModel
import com.shivam.onlinecommerce.ProductInterface
import com.shivam.onlinecommerce.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
//    var activity: Activity? = getActivity()
    lateinit var CategoriesAdapter : CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchbox: SearchView = view.findViewById(R.id.sv_products)
        var searchInput = searchbox.query

        searchbox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                        var intent = Intent(requireContext(), SearchProductActivity::class.java)
                        intent.putExtra("searchInput", searchInput.toString())
                        startActivity(intent)
                } else {
//                        Toast.makeText(this@HomeFragment, "No text in Searchbox", Toast.LENGTH_LONG)
//                            .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })

        var ivCart = view.findViewById<ImageView>(R.id.iv_cart)
        var tvCartCount = view.findViewById<TextView>(R.id.tv_item_count_main)

        recyclerView = view.findViewById(R.id.rv_categories)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)

        val productData = retrofitBuilder.getCategories()

        productData.enqueue(object : Callback<CategoriesModel> {
            override fun onResponse(call: Call<CategoriesModel>, response: Response<CategoriesModel>) {
                var responseBody = response.body()


                if(isAdded && activity != null) {

                    if (responseBody != null) {
                        for (i in 0..responseBody.size){
                            CategoriesAdapter = CategoriesAdapter(requireContext() , responseBody)
                        }
                    }

                    recyclerView.adapter = CategoriesAdapter
                    recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

                    CategoriesAdapter.setOnItemClickListener(object : CategoriesAdapter.OnItemClickListener {
                        @SuppressLint("SuspiciousIndentation")
                        override fun onItemClick(position: Int) {
                            val position = position + 1
//                            Toast.makeText(this@MainActivity, "you clicked on item no. $position", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, CategoryProductActivity::class.java)
                            intent.putExtra("productPosition", position)
                            startActivity(intent)

//                            var cartCount = intent.getIntExtra("count", 0)
//                            tvCartCount.setText("$cartCount")
                        }
                    })
                }

                var url1 = "https://images.gizbot.com/webp/img/2019/10/flipkart-big-billion-days-sale-upto-50-discount-offers-on-t" +
                            "ablets-1570089579.jpg"
                var url2 = "https://img.global.news.samsung.com/in/wp-content/uploads/2022/03/Blue-fest-3000x2000px.jpg"
                var url3 = "https://pbs.twimg.com/media/FEi5zXOVIAIHCcd.jpg:large"
                val SliderDataModelArrayList: ArrayList<String> = ArrayList()
                SliderDataModelArrayList.add(url1)
                SliderDataModelArrayList.add(url2)
                SliderDataModelArrayList.add(url3)

                /*val adapter = SliderAdapter(SliderDataModelArrayList)
                val sliderView = view.findViewById<SliderView>(R.id.slider)
                sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
                sliderView.setSliderAdapter(adapter)
                sliderView.scrollTimeInSec = 3
                sliderView.isAutoCycle = true
                sliderView.startAutoCycle()*/
            }

            override fun onFailure(call: Call<CategoriesModel?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: " + t.message)
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Finish the activity when back button is pressed
                requireActivity().finish()
            }
        })
    }
}