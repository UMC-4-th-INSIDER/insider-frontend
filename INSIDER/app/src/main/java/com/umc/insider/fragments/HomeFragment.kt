package com.umc.insider.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.umc.insider.R
import com.umc.insider.SalesRegistrationActivity
import com.umc.insider.databinding.FragmentHomeBinding
import com.umc.insider.utils.changeStatusBarColor

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusBarColor = ContextCompat.getColor(requireContext(), R.color.statusBarColor)
        activity?.changeStatusBarColor(statusBarColor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()

        return _binding!!.root
    }

    private fun initView(){
        with(binding){
            searchLayout.setOnClickListener {
                val searchFragment = SearchFragment()
                val transaction = parentFragmentManager.beginTransaction()

                transaction.replace(R.id.frame_layout, searchFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }

            // 판매 등록 바로가기
            sellLayout.setOnClickListener {
                val intent = Intent(requireContext(), SalesRegistrationActivity::class.java)
                startActivity(intent)
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}