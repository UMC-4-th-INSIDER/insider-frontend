package com.umc.insider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.umc.insider.databinding.ActivityMainBinding
import com.umc.insider.fragments.ExchangeMainFragment
import com.umc.insider.fragments.FavoriteFragment
import com.umc.insider.fragments.HomeFragment
import com.umc.insider.fragments.MyPageFragment
import com.umc.insider.purchase.PurchaseDetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()
    private val myPageFragment = MyPageFragment()
    private val favoriteFragment = FavoriteFragment()
    private val exchangeMainFragment = ExchangeMainFragment()

    private var isFragmentMain = true
    private var isHomeFragment = true

    // 화면 겹치는 버그, bottomNavigation 안 따라와서 수정
    /*
    override fun onBackPressed() {
        // 바텀 네비게이션의 선택된 아이템이 홈(또는 첫번째)이 아니라면 백스택의 이전 프래그먼트로 이동
        if (binding.bottomNav.selectedItemId != R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                // 백스택에 프래그먼트가 있을 경우, 그 프래그먼트로 이동
                supportFragmentManager.popBackStack()
            } else {
                // 백스택에 프래그먼트가 없는 경우 -> 홈에서 다른 탭으로 이동 후 뒤로가기
                binding.bottomNav.selectedItemId = R.id.home
            }
        } else {
            super.onBackPressed()  // 홈에 있을 경우 기본 뒤로 가기 동작 실행 (앱 백그라운드로 이동)
        }
    }*/

    override fun onBackPressed() {
        currentFragmentisMainFragment()
        // 바텀 네비게이션의 선택된 아이템이 홈(또는 첫번째)이 아니라면 홈으로 이동
        if (binding.bottomNav.selectedItemId != R.id.home && isFragmentMain) {
            binding.bottomNav.selectedItemId = R.id.home
        } else {
            if (isHomeFragment) {
                finish()
            } else {
                super.onBackPressed()  // 홈에 있을 경우 기본 뒤로 가기 동작 실행
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment(homeFragment)

        initView()

    }

    private fun initView() {

        with(binding) {
            bottomNav.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        replaceFragment(homeFragment)
                        return@setOnItemSelectedListener true
                    }

                    R.id.favorite -> {
                        replaceFragment(favoriteFragment)
                        return@setOnItemSelectedListener true
                    }

                    R.id.trade -> {
                        replaceFragment(exchangeMainFragment)
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun currentFragmentisMainFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)

        when (currentFragment) {
            is HomeFragment -> {
                isFragmentMain = true
                isHomeFragment = true
            }

            is FavoriteFragment -> {
                isFragmentMain = true
                isHomeFragment = false
            }

            is ExchangeMainFragment -> {
                isFragmentMain = true
                isHomeFragment = false
            }

            is MyPageFragment -> {
                isFragmentMain = true
                isHomeFragment = false
            }

            else -> {
                isFragmentMain = false
                isHomeFragment = false
            }
        }
    }
}