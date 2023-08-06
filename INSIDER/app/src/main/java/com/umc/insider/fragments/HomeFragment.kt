package com.umc.insider.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.R
import com.umc.insider.adapter.CategoryAdapter
import com.umc.insider.adapter.DiscountGoodsAdapter
import com.umc.insider.databinding.FragmentHomeBinding
import com.umc.insider.model.SearchItem
import com.umc.insider.saleregistraion.SalesRegistrationActivity
import com.umc.insider.utils.changeStatusBarColor

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val imageArray = intArrayOf(R.drawable.category_fruit,R.drawable.category_meat_egg,R.drawable.category_vegetable,R.drawable.category_dairy_product,R.drawable.category_seafood_driedfish,R.drawable.category_etc)
    private val clickImageArray = intArrayOf(R.drawable.category_fruit_click,R.drawable.category_meat_egg_click,R.drawable.category_vegetable_click,R.drawable.category_dairy_product_click,R.drawable.category_seafood_driedfish_click,R.drawable.category_etc_click)
    private val categoryAdapter = CategoryAdapter(imageArray, clickImageArray)
    private val discoutGoodsAdapter = DiscountGoodsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusBarColor = ContextCompat.getColor(requireContext(), R.color.statusBarColor)
        activity?.changeStatusBarColor(statusBarColor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()

        return _binding!!.root
    }

    private fun initView(){
        with(binding){
            searchLayout.setOnClickListener {
                val searchFragment = SearchFragment()
                val transaction = parentFragmentManager.beginTransaction()

                transaction.replace(R.id.frame_layout, searchFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
            //채팅 목록
            messageImg.setOnClickListener {
                val chatListFragment = ChatListFragment()
                val transaction = parentFragmentManager.beginTransaction()

                transaction.replace(R.id.frame_layout, chatListFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }

            // 판매 등록 바로가기
            sellLayout.setOnClickListener {
                val intent = Intent(requireContext(), SalesRegistrationActivity::class.java)
                startActivity(intent)
            }

            pointLayout.setOnClickListener {
                return@setOnClickListener
            }

            categoryRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            categoryRV.addItemDecoration(CategoryAdapterDecoration())
            categoryRV.adapter = categoryAdapter

            todayDiscountRV.adapter = discoutGoodsAdapter
            todayDiscountRV.layoutManager= GridLayoutManager(context, 2)
            todayDiscountRV.addItemDecoration(DiscountAdapterDecoration())
            discoutGoodsAdapter.submitList(DummyDate())

        }


    }

    private fun DummyDate() : ArrayList<SearchItem>{
        val dummy1 = SearchItem(1, "양파1", "100g", "1000원","900원", "10%")
        val dummy2 = SearchItem(2, "양파2", "200g", "2000원","1600원", "20%")
        val dummy3 = SearchItem(3, "양파3", "300g", "3000원","2100원", "30%")
        val dummy4 = SearchItem(4, "양파4", "400g", "4000원","2800원", "30%")
        val dummy5 = SearchItem(5, "양파5", "500g", "6000원","3600원", "40%")
        val dummy6 = SearchItem(6, "양파6", "800g", "8000원","4800원", "40%")

        val arr = ArrayList<SearchItem>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)
        arr.add(dummy6)
        return arr
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

class CategoryAdapterDecoration : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.right = -5
    }
}

class DiscountAdapterDecoration : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if(position % 2 == 0) outRect.right = 10
        else outRect.left = 10

        outRect.bottom = 25
    }
}