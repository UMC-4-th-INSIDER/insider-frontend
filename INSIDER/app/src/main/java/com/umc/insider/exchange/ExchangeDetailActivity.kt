package com.umc.insider.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.umc.insider.R
import com.umc.insider.databinding.ActivityExchangeDetailBinding

class ExchangeDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExchangeDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_exchange_detail)

        val productName = intent.getStringExtra("productName")
        val productAmount = intent.getStringExtra("productAmount")
        val productExchange = intent.getStringExtra("productExchange")

        binding.productName.text = productName
        binding.productAmount.text = productAmount
        binding.exchangeProductName.text = productExchange

    }
}