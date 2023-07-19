package com.umc.insider.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.umc.insider.R
import com.umc.insider.auth.login.LogInActivity
import com.umc.insider.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {

    private var _binding : FragmentMyPageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        initView()

        return binding.root

    }

    private fun initView(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}