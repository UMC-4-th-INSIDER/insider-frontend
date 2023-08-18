package com.umc.insider.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.insider.ChatRoomActivity
import com.umc.insider.R
import com.umc.insider.adapter.ReviewListAdapter
import com.umc.insider.databinding.FragmentReviewListBinding
import com.umc.insider.model.reviewListItem
import com.umc.insider.utils.ChatListClickListener
import com.umc.insider.utils.ReviewListClickListener
import com.umc.insider.utils.changeStatusBarColor
import java.text.SimpleDateFormat
import java.util.Locale


class ReviewListFragment : Fragment(), ReviewListClickListener {

    private var _binding : FragmentReviewListBinding? = null
    private val binding get() = _binding!!
    private lateinit var ReviewListAdapter: ReviewListAdapter

    private val reviewDetailFragment = ReviewDetailFragment() // ReviewDetailFragment 인스턴스 생성

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewListBinding.inflate(inflater, container, false)

        val reviewListData = createSampleReviewList()
        ReviewListAdapter = ReviewListAdapter(reviewListData, this)

        initview()

        return binding.root
    }

    private fun initview(){
        with(binding){
            myReviewListRV.layoutManager = LinearLayoutManager(requireContext())
            myReviewListRV.adapter = ReviewListAdapter
        }
    }

    // Sample data for ChatList (Replace with your actual data)
    private fun createSampleReviewList(): List<reviewListItem> {
        val reviewList = ArrayList<reviewListItem>()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val reviewDate = dateFormat.parse("2023-08-09") // 문자열을 Date 타입으로 변환

        reviewList.add(reviewListItem("##님이 남긴 평점과 후기", "100점", "친절하고 좋았습니다!", reviewDate))
        reviewList.add(reviewListItem("##님이 남긴 평점과 후기", "100점", "친절하고 좋았습니다!", reviewDate))
        reviewList.add(reviewListItem("##님이 남긴 평점과 후기", "100점", "친절하고 좋았습니다!", reviewDate))
        reviewList.add(reviewListItem("##님이 남긴 평점과 후기", "100점", "친절하고 좋았습니다!", reviewDate))
        reviewList.add(reviewListItem("##님이 남긴 평점과 후기", "100점", "친절하고 좋았습니다!", reviewDate))
        reviewList.add(reviewListItem("##님이 남긴 평점과 후기", "100점", "친절하고 좋았습니다!", reviewDate))
        reviewList.add(reviewListItem("##님이 남긴 평점과 후기", "100점", "친절하고 좋았습니다!", reviewDate))


        return reviewList
    }

    override fun onResume() {
        super.onResume()
        binding.root.post {

        }
    }
    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun ReviewListItemClick() {
        // 아이템 클릭 시 ReviewDetailFragment로 이동하는 코드
        val transaction = parentFragmentManager.beginTransaction()

        transaction.replace(R.id.frame_layout, reviewDetailFragment)
        transaction.addToBackStack(null)

        transaction.commit()
    }

}
