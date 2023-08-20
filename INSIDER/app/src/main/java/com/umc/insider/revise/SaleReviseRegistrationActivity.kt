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
import com.umc.insider.retrofit.model.GoodsPostReq
import com.umc.insider.saleregistraion.SaleRegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    private val goods_id = intent.getStringExtra("goods_id")!!.toLong()
    private val GoodsApi = RetrofitInstance.getInstance().create(GoodsInterface::class.java)
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

        initview()
    }

    private fun initview(){
        with(binding){

            // 앞에 있는 화면 터치하면 없어지게 만들기
            frontShadowLayout.setOnClickListener {
                frontShadowLayout.visibility = View.GONE
            }

            lifecycleScope.launch{
                try {
                    val response = withContext(Dispatchers.IO){
                        goodsAPI.getGoodsById(goods_id)
                    }
                    withContext(Dispatchers.Main){
                        sellTitle.setText(response.title)
                        productNameInsert.setText(response.name)
                        productAmountInsert.setText(response.rest)
                        priceExchangeInsert.setText(response.weight)
                        ExpirationDate.text= response.shelf_life

                        Glide.with(binding.sellImageView.context)
                            .load(response.img_url)
                            .placeholder(null)
                            .into(binding.sellImageView)
                    }
                }catch (e : Exception){

                }
            }

            // 갤러리 호출
            sellImageView.setOnClickListener{
                val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
                selectImageResultLauncher.launch(intent)
            }

            // 수정해야 됨.
            sellRegistorBtn.setOnClickListener {
                if(sellTitle.text.isNullOrBlank() || productNameInsert.text.isNullOrBlank() ||
                    productAmountInsert.text.isNullOrBlank() || productWeightInsert.text.isNullOrBlank()
                    || ExpirationDateInsert.text.isNullOrBlank() || priceExchangeInsert.text.isNullOrBlank()
                    || sellLocationInsert.text.isNullOrBlank()){
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

                val gson = Gson()
                val postGoodsReq = GoodsPostReq(
                    title = title,
                    price = priceExchange,
                    rest = productAmount,
                    shelf_life = expirationDate,
                    userIdx = userIdx,
                    name = productName,
                    categoryId = categoryIdx
                )

                val newGoodsJson = gson.toJson(postGoodsReq)
                val newGoodsRequestBody = newGoodsJson.toRequestBody("application/json".toMediaTypeOrNull())

                CoroutineScope(Dispatchers.IO).launch {

                    val imageFile = convertImageUriToPngFile(applicationContext, "name")
                    if(imageFile!=null){
                        val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
                        val token = TokenManager.getToken(applicationContext)
                        try {
                            val response = GoodsApi.createGoods(token!!, newGoodsRequestBody, imagePart)
                            if (response.isSuccessful){
                                withContext(Dispatchers.Main){
                                    Toast.makeText(applicationContext, "상품 등록되었습니다.", Toast.LENGTH_SHORT).show()
                                }

                            }else{
                            }

                        }catch (e : Exception){
                        }
                    }else{
                    }
                }
                finish()
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

    suspend fun convertImageUriToPngFile(context: Context, fileName: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imgUri!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val file = File(context.cacheDir, fileName)
                val outputStream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // 결과 가져오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        // 결과 코드 OK, 결과값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            // 값 담기
            val uri = it.data!!.data

            //화면에 보여주기
            Glide.with(this)
                .load(uri) // 이미지
                .into(binding.sellImageView) // 보여줄 위치
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