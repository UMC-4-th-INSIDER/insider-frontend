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
import com.umc.insider.auth.UserManager
import com.umc.insider.databinding.ActivityPurchaseDetailBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.GoodsInterface
import com.umc.insider.retrofit.api.WishListInterface
import com.umc.insider.retrofit.model.WishListPostReq
import kotlinx.coroutines.CoroutineScope
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

        val user_id = UserManager.getUserIdx(this)!!.toLong()
        val goods_id = intent.getStringExtra("goods_id")!!.toLong()

        val goodsAPI = RetrofitInstance.getInstance().create(GoodsInterface::class.java)
        val wishListAPI = RetrofitInstance.getInstance().create(WishListInterface::class.java)

        val wishListPostReq = WishListPostReq(user_id ,goods_id)

        lifecycleScope.launch {

            try {
                val response = withContext(Dispatchers.IO){
                    wishListAPI.checkWishList(user_id,goods_id)
                }
                withContext(Dispatchers.Main) { binding.favoriteBtn.isChecked = response }
            }catch (e : Exception){

            }
        }

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


        binding.favoriteBtn.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked){

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val response = wishListAPI.addGoodsToWishList(wishListPostReq)

                        if(response.isSuccessful){
                            withContext(Dispatchers.Main){
                                withContext(Dispatchers.Main){
                                    Toast.makeText(this@PurchaseDetailActivity, "찜목록에 추가했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }catch (e : Exception){

                    }
                }

            }else{

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val response = wishListAPI.deleteWishList(userId = user_id, goodsId = goods_id)

                        if(response.isSuccessful){
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@PurchaseDetailActivity, "찜목록에서 삭제했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }else{

                        }

                    }catch (e : Exception){

                    }
                }

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