package com.umc.insider.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.insider.OnNoteListener
import com.umc.insider.R
import com.umc.insider.adapter.SearchResultAdapter
import com.umc.insider.databinding.FragmentSearchResultBinding
import com.umc.insider.model.SearchItem
import com.umc.insider.purchase.PurchaseDetailActivity
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.GoodsInterface
import com.umc.insider.utils.changeStatusBarColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResultFragment : Fragment(), OnNoteListener {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var getResultText: ActivityResultLauncher<Intent>
    private val adapter = SearchResultAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusBarColor = ContextCompat.getColor(requireContext(), R.color.statusBarGreenColor)
        activity?.changeStatusBarColor(statusBarColor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        initView()
        val searchQuery = arguments?.getString("search_query")
        binding.searchText.text = "\"$searchQuery\" 검색 결과"

        val goodsAPI = RetrofitInstance.getInstance().create(GoodsInterface::class.java)

        lifecycleScope.launch {
            Log.d("goods","시작")

            try {
                val response = withContext(Dispatchers.IO) {
                    goodsAPI.getGoods(searchQuery)
                }
                if(response.isSuccess){

                    Log.d("goods",response.result.toString())
                }else{

                    // 에러
                }
            }catch ( e : Exception){ //
                //네트워크나 기타 오류

            }

        }

        return binding.root
    }

    private fun initView() {
        with(binding) {
            searchRV.adapter = adapter
            searchRV.layoutManager = LinearLayoutManager(context)
            searchRV.addItemDecoration(SearchResultAdapterDecoration())
            adapter.submitList(DummyDate())
        }

        getResultText =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // Handle any result if needed
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNotePurchaseDetail(position: Int) {
        val selectedItem = adapter.getItemAtPosition(position)

        val intent = Intent(requireContext(), PurchaseDetailActivity::class.java)
        intent.putExtra("productName", selectedItem.itemName)
        intent.putExtra("productWeight", selectedItem.itemWeight)
        intent.putExtra("productPrice", selectedItem.itemPrice)

        startActivity(intent)
    }

}

class SearchResultAdapterDecoration : RecyclerView.ItemDecoration() {

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