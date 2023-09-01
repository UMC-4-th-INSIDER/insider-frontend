package com.umc.insider.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.OnNoteListener
import com.umc.insider.adapter.GoodsLongAdapter
import com.umc.insider.adapter.SearchResultAdapter
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.FragmentPurchaseCatalogBinding
import com.umc.insider.fragments.SearchResultAdapterDecoration
import com.umc.insider.model.SearchItem
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.ChattingInterface
import com.umc.insider.retrofit.api.GoodsInterface
import kotlinx.coroutines.launch

class PurchaseCatalogFragment : Fragment(), OnNoteListener {

    private var _binding: FragmentPurchaseCatalogBinding? = null
    private val binding get() = _binding!!

    private lateinit var goodsAdapter : GoodsLongAdapter
    private val chattingApi = RetrofitInstance.getInstance().create(ChattingInterface::class.java)

    private var selectPosition : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurchaseCatalogBinding.inflate(inflater, container, false)

        goodsAdapter = GoodsLongAdapter(this)
        val userIdx = UserManager.getUserIdx(requireActivity().applicationContext)!!.toLong()

        lifecycleScope.launch {
            try {
                val response = chattingApi.getGoodsByUser(userIdx)

                if (response.isSuccessful){
                    val map = response.body()
                    val PurchaseGoodsList = map?.get("purchase")
                    goodsAdapter.submitList(PurchaseGoodsList)
                }
            }catch (e : Exception ){

            }
        }

        initview()

        return binding.root
    }

    private fun initview(){
        with(binding){
            PurchaseCatalogRV.adapter = goodsAdapter
            PurchaseCatalogRV.layoutManager = LinearLayoutManager(context)
            PurchaseCatalogRV.addItemDecoration(ShoppingSaleListAdapterDecoration())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onNotePurchaseDetail(goods_id: Long) {
        TODO("Not yet implemented")
    }

}