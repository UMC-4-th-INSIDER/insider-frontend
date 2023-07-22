package com.umc.insider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.umc.insider.auth.login.LogInActivity
import com.umc.insider.databinding.ActivityMainBinding
import com.umc.insider.fragments.HomeFragment
import com.umc.insider.fragments.MyPageFragment
import com.umc.insider.fragments.PurchaseFragment
import com.umc.insider.purchase.PurchaseDetailActivity

class MainActivity : AppCompatActivity(), PurchaseDetailActivity.SellRegistorClickListener {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()
    private val myPageFragment = MyPageFragment()

    override fun onBackPressed() {
        // 바텀 네비게이션의 선택된 아이템이 홈(또는 첫번째)이 아니라면 홈으로 이동
        if (binding.bottomNav.selectedItemId != R.id.home) {
            binding.bottomNav.selectedItemId = R.id.home
        } else {
            super.onBackPressed()  // 홈에 있을 경우 기본 뒤로 가기 동작 실행
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment(homeFragment)

        // 구매하기 버튼 클릭시
        if (intent.getBooleanExtra("SELL_REGISTOR_CLICKED", false)){

            val productName = intent.getStringExtra("productName")
            val productWeight = intent.getStringExtra("productWeight")
            val productPrice = intent.getStringExtra("productPrice")

            val purchaseFragment = PurchaseFragment()

            val args = Bundle()
            args.putString("productName", productName)
            args.putString("productWeight", productWeight)
            args.putString("productPrice", productPrice)
            purchaseFragment.arguments = args

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, purchaseFragment)
                .commit()
        }

        initView()

    }

    private fun initView(){

        with(binding){
            bottomNav.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.home -> {
                        replaceFragment(homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.favorite -> {
                        //replaceFragment(cameraFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.trade -> {
                        //replaceFragment(homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.mypage -> {
                        replaceFragment(myPageFragment)
                        return@setOnItemSelectedListener true
                    }
                    else -> return@setOnItemSelectedListener false
                }

            }



        }
    }
    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }

    override fun onSellRegistorClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, PurchaseFragment())
            .commit()
    }

}