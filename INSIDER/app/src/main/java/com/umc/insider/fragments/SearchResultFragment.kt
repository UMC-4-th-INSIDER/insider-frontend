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
import com.umc.insider.adapter.GoodsLongAdapter
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
    private val goodsAdapter = GoodsLongAdapter(this)

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

            try {
                val response = withContext(Dispatchers.IO) {
                    goodsAPI.getGoods(searchQuery)
                }
                if(response.isSuccess){
                    val goodsList = response.result
                    if (goodsList.isNullOrEmpty()) {
                        Toast.makeText(context, "찾으시는 상품이 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        goodsAdapter.submitList(goodsList)
                    }

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
            searchRV.adapter = goodsAdapter
            searchRV.layoutManager = LinearLayoutManager(context)
            searchRV.addItemDecoration(SearchResultAdapterDecoration())
        }

        getResultText =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // Handle any result if needed
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNotePurchaseDetail(goods_id: Long) {

        //Toast.makeText(requireContext(), goods_id.toString(), Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), PurchaseDetailActivity::class.java)
        intent.putExtra("goods_id", goods_id.toString())
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