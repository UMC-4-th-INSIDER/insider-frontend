package com.umc.insider.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.OnNoteListener
import com.umc.insider.R
import com.umc.insider.adapter.SearchResultAdapter
import com.umc.insider.databinding.FragmentSellCatalogBinding
import com.umc.insider.model.SearchItem

class SellCatalogFragment : Fragment(), OnNoteListener {

    private var _binding: FragmentSellCatalogBinding? = null
    private val binding get() = _binding!!

    private val sellCatalogAdapter = SearchResultAdapter(this)

    private var isStatusSelect = true
    private var isDecorateCheck = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellCatalogBinding.inflate(inflater, container, false)

        initview()

        return binding.root
    }

    private fun initview(){
        with(binding){

            sellIng.setOnClickListener {
                isStatusSelect = true
                updateButtonUI()
            }

            sellEnd.setOnClickListener {
                isStatusSelect = false
                updateButtonUI()
            }

            updateButtonUI()
            isDecorateCheck = false
        }
    }

    private fun updateButtonUI() {
        with(binding) {

            if(isStatusSelect){
                sellIng.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_left_round)
                sellIng.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                sellEnd.background = ContextCompat.getDrawable(requireContext(), R.drawable.white_right_round)
                sellEnd.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))

                sellCatalogRV.adapter = sellCatalogAdapter
                sellCatalogRV.layoutManager = LinearLayoutManager(context)
                if(isDecorateCheck){
                    sellCatalogRV.addItemDecoration(SearchResultAdapterDecoration())
                }
                sellCatalogAdapter.submitList(GeneralDummyDate())
            }else{
                sellIng.background = ContextCompat.getDrawable(requireContext(), R.drawable.white_left_round)
                sellIng.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
                sellEnd.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_right_round)
                sellEnd.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                // 서버 연결하면 변경
                sellCatalogRV.adapter = sellCatalogAdapter
                sellCatalogRV.layoutManager = LinearLayoutManager(context)
                if(isDecorateCheck){
                    sellCatalogRV.addItemDecoration(SearchResultAdapterDecoration())
                }
                sellCatalogAdapter.submitList(GeneralDummyDate())
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


    override fun onNotePurchaseDetail(goods_id: Long) {
        TODO("Not yet implemented")
    }


}