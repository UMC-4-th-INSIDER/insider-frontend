package com.umc.insider.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.umc.insider.R
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.ActivityExchangeDetailBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.ExchangesInterface
import com.umc.insider.retrofit.api.GoodsInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExchangeDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExchangeDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_exchange_detail)

        val user_id = UserManager.getUserIdx(this)!!.toLong()
        val goods_id = intent.getStringExtra("goods_id")!!.toLong()

        val exchangesAPI = RetrofitInstance.getInstance().create(ExchangesInterface::class.java)

        lifecycleScope.launch {

            try {

                val response = exchangesAPI.getGoodsById(goods_id)

                withContext(Dispatchers.Main){

                    Glide.with(binding.productImage.context)
                        .load(response.imageUrl)
                        .placeholder(null)
                        .transform(RoundedCorners(30))
                        .into(binding.productImage)

                    binding.productNameTitle.text = response.title
                    binding.productName.text = response.name
                    if (response.weight.isNullOrBlank()){
                        binding.productAmount.text = "(${response.count}개)"
                        binding.PurchaseTotalamountTv.text = "${response.count}g"
                    }else{
                        binding.productAmount.text = "(${response.weight}g)"
                        binding.PurchaseTotalamountTv.text = "${response.count}개"
                    }
                    binding.PurchaseExpirationDate.text = response.shelfLife
                    binding.purchaseLocation.text = "아직없음"
                    binding.sellerInfo.text = "올린 사람"

                }


            }catch (e : Exception){

            }
        }

    }
}