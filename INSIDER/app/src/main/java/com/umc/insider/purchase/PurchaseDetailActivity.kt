package com.umc.insider.purchase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.umc.insider.MainActivity
import com.umc.insider.R
import com.umc.insider.databinding.ActivityPurchaseDetailBinding

class PurchaseDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPurchaseDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase_detail)

        // Get the intent extras
        val productName = intent.getStringExtra("productName")
        val productWeight = "(" + intent.getStringExtra("productWeight") + ")"
        val productPrice = intent.getStringExtra("productPrice")

        // Set the data to the views
        binding.productName.text = productName
        binding.productWeight.text = productWeight
        binding.productPrice.text = productPrice

        // 구매하기 버튼 클릭시 mainActivity로 넘어가고 Fragment는 구매하기 PurchaseFragment
        binding.sellRegistorBtn.setOnClickListener {
            val intent = Intent(this@PurchaseDetailActivity, PurchaseActivity::class.java)
            intent.putExtra("SELL_REGISTOR_CLICKED", true)
            intent.putExtra("productName", binding.productName.text)
            intent.putExtra("productWeight", binding.productWeight.text)
            intent.putExtra("productPrice", binding.productPrice.text)
            startActivity(intent)
            finish()
        }

    }

    interface SellRegistorClickListener{
        fun onSellRegistorClicked()
    }

}