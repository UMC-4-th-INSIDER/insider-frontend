package com.umc.insider.purchase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.umc.insider.R
import com.umc.insider.databinding.ActivityPurchaseDetailBinding

class PurchaseDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPurchaseDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase_detail)

        // 구매하기 버튼 클릭시 mainActivity로 넘어가고 Fragment는 구매하기 PurchaseFragment

    }
}