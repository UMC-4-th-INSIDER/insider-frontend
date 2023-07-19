package com.umc.insider

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.fido.fido2.api.common.RequestOptions
import com.umc.insider.databinding.ActivitySalesRegistrationBinding
import com.umc.insider.utils.SaleRegistrationViewModel
import java.io.File

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