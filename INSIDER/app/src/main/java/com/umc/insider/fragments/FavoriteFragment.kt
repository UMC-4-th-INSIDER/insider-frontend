package com.umc.insider.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.OnNoteListener
import com.umc.insider.R
import com.umc.insider.adapter.ExchangeAdapter
import com.umc.insider.adapter.GoodsLongAdapter
import com.umc.insider.adapter.SearchResultAdapter
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.FragmentFavoriteBinding
import com.umc.insider.model.ExchangeItem
import com.umc.insider.model.SearchItem
import com.umc.insider.purchase.PurchaseDetailActivity
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.GoodsInterface
import com.umc.insider.retrofit.api.WishListInterface
import com.umc.insider.revise.SaleReviseDetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment(), OnNoteListener {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    //private val searchAdapter = SearchResultAdapter(this)
    private val generalPurchasefavoriteAdapter = GoodsLongAdapter(this)
    private val exchangeAdapter = ExchangeAdapter(this)

    private val wishListAPI = RetrofitInstance.getInstance().create(WishListInterface::class.java)

    private var isGeneralPurchaseSelected = true
    private var isDecorateCheck = true
    private var user_id : Long? = null

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        user_id = UserManager.getUserIdx(requireContext())!!.toLong()
        initview()

        return binding.root
    }

    private fun initview() {

        with(binding) {
            // Click listeners for the buttons

            favoriteRV.adapter = generalPurchasefavoriteAdapter
            favoriteRV.layoutManager = LinearLayoutManager(context)
            favoriteRV.addItemDecoration(ExchangeMainFragment.ExchangeAdapterDecoration())

            selectPurchase.setOnClickListener {
                isGeneralPurchaseSelected = true
                updateButtonUI()
            }

            Exchange.setOnClickListener {
                isGeneralPurchaseSelected = false
                updateButtonUI()
            }

            // Update the button UI initially
            updateButtonUI()
            isDecorateCheck = false
        }
    }

    private fun updateButtonUI() {
        with(binding) {

            // 서버 넘겨받으면 찜한 목록 중에서 어떤 건지 판단해서 recyclerview 띄우게 하기
            if (isGeneralPurchaseSelected) {

                lifecycleScope.launch {

                    try {

                        val response = withContext(Dispatchers.IO){
                            wishListAPI.getGoodsInWishList(user_id!!)
                        }

                        if (response.isSuccessful){
                            val generalPurchasefavoriteGoodsList = response.body()
                            withContext(Dispatchers.Main){
                                generalPurchasefavoriteAdapter.submitList(generalPurchasefavoriteGoodsList)
                            }
                        }

                    }catch (e : Exception){

                    }

                }


            } else {
                generalPurchase.background = ContextCompat.getDrawable(requireContext(), R.drawable.white_left_round)
                generalPurchase.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
                Exchange.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_right_round)
                Exchange.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                favoriteRV.adapter = exchangeAdapter
                favoriteRV.layoutManager = LinearLayoutManager(context)
                if(isDecorateCheck){
                    //favoriteRV.addItemDecoration(ExchangeMainFragment.ExchangeAdapterDecoration())
                }
                exchangeAdapter.submitList(ExchangeDummyDate())
            }
        }
    }

    fun refreshData(){
        lifecycleScope.launch {

            try {

                val response = withContext(Dispatchers.IO){
                    wishListAPI.getGoodsInWishList(user_id!!)
                }

                if (response.isSuccessful){
                    val generalPurchasefavoriteGoodsList = response.body()
                    withContext(Dispatchers.Main){
                        generalPurchasefavoriteAdapter.submitList(generalPurchasefavoriteGoodsList)
                    }
                }

            }catch (e : Exception){

            }

        }
    }

    override fun onPause(){
        super.onPause()
        isDecorateCheck = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun GeneralDummyDate(): ArrayList<SearchItem> {
        val dummy1 = SearchItem(1, "양파1", "100g", "1000원", null, null)
        val dummy2 = SearchItem(2, "양파2", "200g", "2000원", null, null)
        val dummy3 = SearchItem(3, "양파3", "300g", "2800원", null, null)
        val dummy4 = SearchItem(4, "양파4", "400g", "3800원", null, null)
        val dummy5 = SearchItem(5, "양파5", "500g", "4500원", null, null)

        val arr = ArrayList<SearchItem>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)

        return arr
    }

    private fun ExchangeDummyDate(): ArrayList<ExchangeItem> {
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


    override fun onNotePurchaseDetail(goods_id: Long) {
        val userIdx = UserManager.getUserIdx(requireActivity().applicationContext)!!.toLong()
        val goodsAPI = RetrofitInstance.getInstance().create(GoodsInterface::class.java)
        Log.d("REVISEEE", "userIdx : {$userIdx}")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    goodsAPI.getGoodsById(goods_id)
                }
                withContext(Dispatchers.Main){
                    val sellerID = response.users_id.id

                    if(userIdx != sellerID){
                        val intent = Intent(requireContext(), PurchaseDetailActivity::class.java)
                        intent.putExtra("goods_id", goods_id.toString())
                        startActivity(intent)
                    } else {
                        val intent = Intent(requireContext(), SaleReviseDetailActivity::class.java)
                        intent.putExtra("goods_id", goods_id.toString())
                        startActivity(intent)
                    }
                }
            }catch (e : Exception){

            }

        }
    }
}

