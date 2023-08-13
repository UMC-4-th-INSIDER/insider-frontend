package com.umc.insider.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.R
import com.umc.insider.adapter.CategoryImgAdapter
import com.umc.insider.adapter.GoodsLongAdapter
import com.umc.insider.databinding.FragmentCategoryResultBinding
import com.umc.insider.databinding.FragmentHomeBinding
import com.umc.insider.utils.CategoryClickListener

class CategoryResultFragment : Fragment(), CategoryClickListener {

    private var _binding : FragmentCategoryResultBinding? = null
    private val binding get() = _binding!!

    private val imageArray = intArrayOf(R.drawable.category_fruit,R.drawable.category_meat_egg,R.drawable.category_vegetable,R.drawable.category_dairy_product,R.drawable.category_seafood_driedfish,R.drawable.category_etc)
    private val clickImageArray = intArrayOf(R.drawable.category_fruit_click,R.drawable.category_meat_egg_click,R.drawable.category_vegetable_click,R.drawable.category_dairy_product_click,R.drawable.category_seafood_driedfish_click,R.drawable.category_etc_click)
    private val categoryTextArray = mutableListOf<String>("과일", "정육/계란", "채소", "유제품", "수산/건어물", "기타")

    private lateinit var categoryImgAdapter : CategoryImgAdapter
    private val goodsAdapter = GoodsLongAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryResultBinding.inflate(inflater, container, false)

        val selectPosition = arguments?.getString("select_category")!!.toInt()
        categoryImgAdapter = CategoryImgAdapter(imageArray,clickImageArray, selectPosition, this)

        initView()

        return binding.root
    }

    private fun initView(){

        with(binding){
            categoryRecyclerView.adapter = categoryImgAdapter
            categoryRecyclerView.layoutManager = GridLayoutManager(context, 6, GridLayoutManager.VERTICAL, false )

            categorySearchRV.adapter = goodsAdapter
            categorySearchRV.layoutManager = LinearLayoutManager(context)
            categorySearchRV.addItemDecoration(SearchResultAdapterDecoration())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onImageTouch(position: Int) {
        // 여기서 api 호출 - category
        Toast.makeText(context, categoryTextArray[position], Toast.LENGTH_SHORT).show()
    }
}