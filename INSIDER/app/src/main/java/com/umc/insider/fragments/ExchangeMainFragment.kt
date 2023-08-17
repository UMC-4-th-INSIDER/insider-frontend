package com.umc.insider.fragments

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.OnNoteListener
import com.umc.insider.R
import com.umc.insider.adapter.CategoryAdapter
import com.umc.insider.adapter.CategoryImgAdapter
import com.umc.insider.adapter.ExchangeAdapter
import com.umc.insider.adapter.GoodsLongAdapter
import com.umc.insider.databinding.FragmentExchangeMainBinding
import com.umc.insider.exchange.ExchangeDetailActivity
import com.umc.insider.model.ExchangeItem
import com.umc.insider.model.SearchItem
import com.umc.insider.purchase.PurchaseDetailActivity
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.GoodsInterface
import com.umc.insider.utils.CategoryClickListener
import kotlinx.coroutines.launch

class ExchangeMainFragment : Fragment(), CategoryClickListener, OnNoteListener {

    private var _binding : FragmentExchangeMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var getResultText: ActivityResultLauncher<Intent>
    private val adapter = ExchangeAdapter(this)

    private val imageArray = intArrayOf(R.drawable.category_fruit,R.drawable.category_meat_egg,R.drawable.category_vegetable,R.drawable.category_dairy_product,R.drawable.category_seafood_driedfish,R.drawable.category_etc)
    private val clickImageArray = intArrayOf(R.drawable.category_fruit_click,R.drawable.category_meat_egg_click,R.drawable.category_vegetable_click,R.drawable.category_dairy_product_click,R.drawable.category_seafood_driedfish_click,R.drawable.category_etc_click)

    private val categoryAdapter = CategoryAdapter(imageArray, clickImageArray, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExchangeMainBinding.inflate(inflater, container, false)

        initview()

        return binding.root
    }

    private fun initview(){
        with(binding){
            searchRV.adapter = adapter
            searchRV.layoutManager = LinearLayoutManager(context)
            searchRV.addItemDecoration(ExchangeAdapterDecoration())
            adapter.submitList(DummyDate())

            categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            categoryRecyclerView.addItemDecoration(CategoryAdapterDecoration())
            categoryRecyclerView.adapter = categoryAdapter

        }

        getResultText =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Handle any result if needed
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun DummyDate(): ArrayList<ExchangeItem> {
        val dummy1 = ExchangeItem(1, "군양파", "1개", "당근", "")
        val dummy2 = ExchangeItem(2, "시양파", "2개", "씨앗", "")
        val dummy3 = ExchangeItem(3, "도양파", "3개", "코코넛", "")
        val dummy4 = ExchangeItem(4, "섬양파", "4개", "해삼", "")
        val dummy5 = ExchangeItem(5, "바다양파", "5개", "말미잘", "")

        val arr = ArrayList<ExchangeItem>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)

        return arr
    }

    class ExchangeAdapterDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = 20
        }
    }

    override fun onNotePurchaseDetail(goods_id: Long) {

    }

    override fun onImageTouch(position: Int) {
        TODO("Not yet implemented")
    }

}