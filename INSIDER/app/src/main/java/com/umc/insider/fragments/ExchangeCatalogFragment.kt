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
import com.umc.insider.adapter.ExchangeAdapter
import com.umc.insider.adapter.ExchangeEndAdapter
import com.umc.insider.adapter.ExchangeEndCatalogAdapter
import com.umc.insider.databinding.FragmentExchangeCatalogBinding
import com.umc.insider.model.ExchangeItem


class ExchangeCatalogFragment : Fragment(), OnNoteListener {

    private var _binding : FragmentExchangeCatalogBinding? = null
    private val binding get() = _binding!!

    private val exchangingAdapter = ExchangeAdapter(this)
    private val exchangeEndAdapter = ExchangeEndCatalogAdapter(this)

    private var isSelect = true
    private var isDecorateCheck = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExchangeCatalogBinding.inflate(inflater, container, false)

        initview()

        return binding.root
    }

    private fun initview(){
        with(binding){

            exchangeIng.setOnClickListener {
                isSelect = true
                updateButtonUI()
            }

            exchangeEnd.setOnClickListener {
                isSelect = false
                updateButtonUI()
            }

            updateButtonUI()
            isDecorateCheck = false
        }
    }

    private fun updateButtonUI() {
        with(binding) {

            // 서버 넘겨받으면 찜한 목록 중에서 어떤 건지 판단해서 recyclerview 띄우게 하기
            if (isSelect) {
                exchangeIng.background = ContextCompat.getDrawable(requireContext(),
                    R.drawable.green_left_round
                )
                exchangeIng.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                exchangeEnd.background = ContextCompat.getDrawable(requireContext(),
                    R.drawable.white_right_round
                )
                exchangeEnd.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))

                exchangeCatalogRV.adapter = exchangingAdapter
                exchangeCatalogRV.layoutManager = LinearLayoutManager(context)
                if(isDecorateCheck){
                    exchangeCatalogRV.addItemDecoration(SearchResultAdapterDecoration())
                }
                exchangingAdapter.submitList(ExchangeDummyDate())
            } else {
                exchangeIng.background = ContextCompat.getDrawable(requireContext(),
                    R.drawable.white_left_round
                )
                exchangeIng.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
                exchangeEnd.background = ContextCompat.getDrawable(requireContext(),
                    R.drawable.green_right_round
                )
                exchangeEnd.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                exchangeCatalogRV.adapter = exchangeEndAdapter
                exchangeCatalogRV.layoutManager = LinearLayoutManager(context)
                if(isDecorateCheck){
                    exchangeCatalogRV.addItemDecoration(ExchangeMainFragment.ExchangeAdapterDecoration())
                }
                exchangeEndAdapter.submitList(ExchangeDummyDate())
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

    private fun ExchangeDummyDate(): ArrayList<ExchangeItem> {
        val dummy1 = ExchangeItem(1, "군양파", "1개", "당근", "1개")
        val dummy2 = ExchangeItem(2, "시양파", "2개", "씨앗", "2개")
        val dummy3 = ExchangeItem(3, "도양파", "3개", "코코넛", "3개")
        val dummy4 = ExchangeItem(4, "섬양파", "4개", "해삼", "4개")
        val dummy5 = ExchangeItem(5, "바다양파", "5개", "말미잘", "5개")

        val arr = ArrayList<ExchangeItem>()
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