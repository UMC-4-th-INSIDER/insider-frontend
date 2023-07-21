package com.umc.insider.saleregistraion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.umc.insider.R
import com.umc.insider.databinding.ActivitySalesRegistrationBinding

class SalesRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesRegistrationBinding
    private lateinit var viewModel : SaleRegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sales_registration)
        viewModel = ViewModelProvider(this)[SaleRegistrationViewModel::class.java]

        binding.sellvm = viewModel
        binding.lifecycleOwner = this

        binding.sellImageView.setOnClickListener{

            // 갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        binding.categorySpinner.adapter = ArrayAdapter.createFromResource(
            this, R.array.categoryList, android.R.layout.simple_spinner_item)

        // 판매 등록하기 버튼
        binding.sellRegistorBtn.setOnClickListener {




            // 판매 등록한 다음에 어떤걸 원하시는지?

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

}