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
import com.umc.insider.databinding.FragmentFavoriteBinding
import com.umc.insider.model.SearchItem

class FavoriteFragment : Fragment(), OnNoteListener {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val adapter = SearchResultAdapter(this)

    private var isGeneralPurchaseSelected = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        initview()

        return binding.root
    }

    private fun initview() {
        with(binding) {
            favoriteRV.adapter = adapter
            favoriteRV.layoutManager = LinearLayoutManager(context)
            favoriteRV.addItemDecoration(SearchResultAdapterDecoration())
            adapter.submitList(DummyDate())

            // Click listeners for the buttons
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
        }
    }

    private fun updateButtonUI() {
        with(binding) {

            // 서버 넘겨받으면 찜한 목록 중에서 어떤 건지 판단해서 recyclerview 띄우게 하기
            if (isGeneralPurchaseSelected) {
                generalPurchase.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_left_round)
                generalPurchase.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                Exchange.background = ContextCompat.getDrawable(requireContext(), R.drawable.white_right_round)
                Exchange.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            } else {
                generalPurchase.background = ContextCompat.getDrawable(requireContext(), R.drawable.white_left_round)
                generalPurchase.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
                Exchange.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_right_round)
                Exchange.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun DummyDate(): ArrayList<SearchItem> {
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

    override fun onNotePurchaseDetail(position: Int) {
        TODO("Not yet implemented")
    }
}

