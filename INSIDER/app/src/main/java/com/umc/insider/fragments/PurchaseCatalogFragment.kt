package com.umc.insider.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.OnNoteListener
import com.umc.insider.adapter.SearchResultAdapter
import com.umc.insider.databinding.FragmentPurchaseCatalogBinding
import com.umc.insider.fragments.SearchResultAdapterDecoration
import com.umc.insider.model.SearchItem

class PurchaseCatalogFragment : Fragment(), OnNoteListener {

    private var _binding: FragmentPurchaseCatalogBinding? = null
    private val binding get() = _binding!!

    private val purchaseCatalogAdapter = SearchResultAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurchaseCatalogBinding.inflate(inflater, container, false)

        initview()

        return binding.root
    }

    private fun initview(){
        with(binding){
            PurchaseCatalogRV.adapter = purchaseCatalogAdapter
            PurchaseCatalogRV.layoutManager = LinearLayoutManager(context)
            PurchaseCatalogRV.addItemDecoration(SearchResultAdapterDecoration())
            purchaseCatalogAdapter.submitList(GeneralDummyDate())
        }
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

    override fun onNotePurchaseDetail(position: Int) {
        TODO("Not yet implemented")
    }

}