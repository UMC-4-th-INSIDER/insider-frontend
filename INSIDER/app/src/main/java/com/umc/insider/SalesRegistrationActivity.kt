package com.umc.insider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.umc.insider.databinding.ActivitySalesRegistrationBinding

class SalesRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sales_registration)




    }

    companion object{
        const val REVIEW_MIN_LENGTH = 10
        // 갤러리 권한 요청

    }

}