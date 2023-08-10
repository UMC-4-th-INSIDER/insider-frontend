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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.OnNoteListener
import com.umc.insider.R
import com.umc.insider.adapter.ExchangeAdapter
import com.umc.insider.databinding.FragmentExchangeMainBinding
import com.umc.insider.exchange.ExchangeDetailActivity
import com.umc.insider.model.ExchangeItem
import com.umc.insider.model.SearchItem
import com.umc.insider.purchase.PurchaseDetailActivity

class ExchangeMainFragment : Fragment(), OnNoteListener {

    private var _binding : FragmentExchangeMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var getResultText: ActivityResultLauncher<Intent>
    private val adapter = ExchangeAdapter(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

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
        }

        getResultText =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Handle any result if needed
                }
            }
    }

    private fun DummyDate(): ArrayList<ExchangeItem> {
        val dummy1 = ExchangeItem(1, "군양파", "1개", "당근")
        val dummy2 = ExchangeItem(2, "시양파", "2개", "씨앗")
        val dummy3 = ExchangeItem(3, "도양파", "3개", "코코넛")
        val dummy4 = ExchangeItem(4, "섬양파", "4개", "해삼")
        val dummy5 = ExchangeItem(5, "바다양파", "5개", "말미잘")

        val arr = ArrayList<ExchangeItem>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)

        return arr
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExchangeMainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onNotePurchaseDetail(position: Int) {
        val selectedItem = adapter.getItemAtPosition(position)

        val intent = Intent(requireContext(), ExchangeDetailActivity::class.java)
        intent.putExtra("productName", selectedItem.itemName)
        intent.putExtra("productAmount", selectedItem.itemAmount)
        intent.putExtra("productExchange", selectedItem.itemExchange)

        startActivity(intent)
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

}