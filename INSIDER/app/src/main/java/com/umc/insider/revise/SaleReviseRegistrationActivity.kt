package com.umc.insider.revise

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.umc.insider.R
import com.umc.insider.adapter.CustomSpinnerAdapter
import com.umc.insider.auth.TokenManager
import com.umc.insider.auth.UserManager
import com.umc.insider.auth.signUp.AddressActivity
import com.umc.insider.databinding.ActivitySaleReviseRegistrationBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.GoodsInterface
import com.umc.insider.retrofit.model.GoodsPostModifyPriceReq
import com.umc.insider.retrofit.model.GoodsPostReq
import com.umc.insider.retrofit.response.BaseResponse
import com.umc.insider.saleregistraion.SaleRegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class SaleReviseRegistrationActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySaleReviseRegistrationBinding
    private lateinit var viewModel : SaleRegistrationViewModel
    private lateinit var getSearchResult : ActivityResultLauncher<Intent>

    private lateinit var categorySpinner: Spinner
    private lateinit var adapter: CustomSpinnerAdapter

    private var imgUri : Uri? = null

    private val selectImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imgUri = result.data?.data
            Glide.with(this)
                .load(imgUri)
                .into(binding.sellImageView)
        }
    }

    private val goodsAPI = RetrofitInstance.getInstance().create(GoodsInterface::class.java)

    private var isGeneralSaleSelected = true    // true면 일반 판매

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sale_revise_registration)

        getSearchResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val fulladdress = it.data?.getStringExtra("data")
            if (!fulladdress.isNullOrBlank()){
                val addressSplit = fulladdress.split(",")
                binding.sellLocationInsert.text = addressSplit[0]
            }
        }

        val categories = listOf("카테고리", "과일", "정육/계란", "채소", "유제품", "수산/건어물", "기타")
        categorySpinner = findViewById(R.id.categorySpinner)
        adapter = CustomSpinnerAdapter(this, categories)
        categorySpinner.adapter = adapter

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val goods_id = intent.getStringExtra("goods_id")!!.toLong()

        lifecycleScope.launch{
            try {
                val response = withContext(Dispatchers.IO){
                    goodsAPI.getGoodsById(goods_id)
                }
                withContext(Dispatchers.Main){
                    Log.d("productININ", "${response.title}")
                    Log.d("productININ", "name : ${response.name}")
                    Log.d("productININ", "rest : ${response.rest}")
                    Log.d("productININ", "price : ${response.price}")
                    Log.d("productININ", "weight : ${response.weight}")
                    Log.d("productININ", "shelf_life : ${response.shelf_life}")
                    Log.d("productININ", "img_url : ${response.img_url}")

                    binding.sellTitle.setText(response.title)
                    binding.productNameInsert.setText(response.name)
                    binding.productAmountInsert.setText(response.rest.toString())
                    binding.priceExchangeInsert.setText(response.price)
                    binding.productWeightInsert.setText(response.weight!!)
                    binding.ExpirationDateInsert.setText(response.shelf_life)

                    Glide.with(binding.sellImageView.context)
                        .load(response.img_url)
                        .placeholder(null)
                        .into(binding.sellImageView)
                }
            }catch (e : Exception){

            }
        }

        // 수정해야하는지 확인 필요
        binding.sellRegistorBtn.setOnClickListener {
            if(binding.sellTitle.text.isNullOrBlank() || binding.productNameInsert.text.isNullOrBlank() ||
                binding.productAmountInsert.text.isNullOrBlank() || binding.ExpirationDateInsert.text.isNullOrBlank()
                || binding.priceExchangeInsert.text.isNullOrBlank() || binding.sellLocationInsert.text.isNullOrBlank()){
                Toast.makeText(applicationContext, "빈 항복을 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val title = binding.sellTitle.text.toString()
            val productName = binding.productNameInsert.text.toString()
            val productAmount = binding.productAmountInsert.text.toString().toIntOrNull()
            val productWeight = binding.productWeightInsert.text.toString().toFloatOrNull()
            val expirationDate = binding.ExpirationDateInsert.text.toString()
            val priceExchange = binding.priceExchangeInsert.text.toString()
            val location = binding.sellLocationInsert.text.toString()
            val userIdx = UserManager.getUserIdx(applicationContext)!!.toLong()
            val categoryIdx = (binding.categorySpinner.selectedItemPosition).toLong()
            Log.d("category", categoryIdx.toString())

            val postModifyPriceReq = GoodsPostModifyPriceReq(
                id = goods_id,
                price = priceExchange
            )

            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO){
                        goodsAPI.modifyPrice(postModifyPriceReq)
                    }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val result = response.body()?.result
                            if (result != null) {
                                Toast.makeText(applicationContext, "수정을 완료하였습니다!", Toast.LENGTH_SHORT)
                                Log.d("modifyyy", "요청 성공")
                            } else {
                                Log.d("modifyyy", "요청 실패")
                            }
                        } else {
                            Log.d("modifyyy", "네트워크 오류")
                        }
                    }
                } catch (e: Exception) {
                    Log.d("modifyyy", e.toString())
                }
            }


            finish()
        }

        initview()
    }

    private fun initview(){
        with(binding){

            // 앞에 있는 화면 터치하면 없어지게 만들기
            frontShadowLayout.setOnClickListener {
                frontShadowLayout.visibility = View.GONE
            }



            // 갤러리 호출
            sellImageView.setOnClickListener{
                val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
                selectImageResultLauncher.launch(intent)
            }



            // 우편번호 인증
            addressFindBtn.setOnClickListener {
                val intent = Intent(this@SaleReviseRegistrationActivity, AddressActivity::class.java)
                getSearchResult.launch(intent)
            }

            sellLocationInsert.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // 아무것도 하지 않음
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (!sellLocationInsert.text.isNullOrBlank()) {
                        addressFindBtn.text = "주소인증 완료"
                        val tint = ContextCompat.getColor(this@SaleReviseRegistrationActivity,
                            R.color.lightMain
                        )
                        DrawableCompat.setTint(addressFindBtn.background, tint)
                    } else {
                        addressFindBtn.text = "우편번호 인증"
                        val tint = ContextCompat.getColor(this@SaleReviseRegistrationActivity,
                            R.color.gray30
                        )
                        DrawableCompat.setTint(addressFindBtn.background, tint)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    // 아무것도 하지 않음
                }
            })

            // 일반 구매, 교환하기 설정
            generalSale.setOnClickListener {
                isGeneralSaleSelected = true    // 일반 구매
                updateButtonUI()
                productAmountTv.text = "판매 갯수"
                productAmountInsert.hint = "판매 갯수를 입력하세요."
                val params = priceExchangeTv.layoutParams
                params.width = dpToPx(90)
                priceExchangeTv.layoutParams = params
                priceExchangeTv.text = "개당 가격"
                priceExchangeInsert.hint = "개당 판매 가격을 입력하세요."
            }

            Exchange.setOnClickListener {
                isGeneralSaleSelected = false   // 교환하기
                updateButtonUI()
                productAmountTv.text = "교환 갯수"
                productAmountInsert.hint = "교환 갯수를 입력하세요."
                val params = priceExchangeTv.layoutParams
                params.width = dpToPx(150)
                priceExchangeTv.layoutParams = params
                priceExchangeTv.text = "원하는 교환 품목"
                priceExchangeInsert.hint = "ex. 당근"
            }

            updateButtonUI()

        }

        // 카테고리
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                adapter.setSelectedItemVisibility(position == 0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }

    // 일반 판매, 교환하기 선택
    private fun updateButtonUI() {
        with(binding) {

            // 서버 넘겨받으면 찜한 목록 중에서 어떤 건지 판단해서 recyclerview 띄우게 하기
            if (isGeneralSaleSelected) {
                generalSale.background = ContextCompat.getDrawable(this@SaleReviseRegistrationActivity,
                    R.drawable.green_left_round
                )
                generalSale.setTextColor(ContextCompat.getColor(this@SaleReviseRegistrationActivity,
                    R.color.white
                ))
                Exchange.background = ContextCompat.getDrawable(this@SaleReviseRegistrationActivity,
                    R.drawable.white_right_round
                )
                Exchange.setTextColor(ContextCompat.getColor(this@SaleReviseRegistrationActivity,
                    R.color.main
                ))
            } else {
                generalSale.background = ContextCompat.getDrawable(this@SaleReviseRegistrationActivity,
                    R.drawable.white_left_round
                )
                generalSale.setTextColor(ContextCompat.getColor(this@SaleReviseRegistrationActivity,
                    R.color.main
                ))
                Exchange.background = ContextCompat.getDrawable(this@SaleReviseRegistrationActivity,
                    R.drawable.green_right_round
                )
                Exchange.setTextColor(ContextCompat.getColor(this@SaleReviseRegistrationActivity,
                    R.color.white
                ))
            }
        }
    }

    fun Context.dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}