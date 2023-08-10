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


    }
}