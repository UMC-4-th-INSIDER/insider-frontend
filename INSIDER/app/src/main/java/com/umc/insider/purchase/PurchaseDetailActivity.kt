package com.umc.insider.purchase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.umc.insider.MainActivity
import com.umc.insider.R
import com.umc.insider.databinding.ActivityPurchaseDetailBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.GoodsInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PurchaseDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPurchaseDetailBinding

    // true라면 상품 올린 사람, false라면 다른 사람
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase_detail)

        val goods_id = intent.getStringExtra("goods_id")!!.toLong()

        val goodsAPI = RetrofitInstance.getInstance().create(GoodsInterface::class.java)

        lifecycleScope.launch {

            try {
                val response = withContext(Dispatchers.IO){
                    goodsAPI.getGoodsById(goods_id)
                }
                withContext(Dispatchers.Main){
                    binding.productNameTitle.text = response.title
                    binding.productName.text = response.name
                    binding.PurchaseUnitamountTv.text = null
                    if(response.weight.isNullOrBlank()){
                        binding.PurchaseTotalamountTv.text = "${response.rest}개"
                        binding.productUnit.text = "(개당)"
                    }else{
                        binding.PurchaseTotalamountTv.text = "${response.weight}g"
                        binding.productUnit.text = "(100g당)"
                    }
                    binding.PurchaseExpirationDate.text= response.shelf_life
                    binding.sellerInfo.text = response.users_id.nickname
                    binding.productPrice.text = "${response.price}원"

                    Glide.with(binding.productImage.context)
                        .load(response.img_url)
                        .placeholder(null)
                        .into(binding.productImage)
                }
            }catch (e : Exception){

            }

        }

        binding.sellRegistorBtn.setOnClickListener {
            val intent = Intent(this@PurchaseDetailActivity, PurchaseActivity::class.java)
            intent.putExtra("goods_id", goods_id.toString())
            startActivity(intent)
            finish()
        }
    }

}