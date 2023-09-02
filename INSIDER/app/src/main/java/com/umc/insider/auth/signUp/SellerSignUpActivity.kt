package com.umc.insider.auth.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.umc.insider.R
import com.umc.insider.auth.login.LogInActivity
import com.umc.insider.databinding.ActivitySellerSignUpBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.UserInterface
import com.umc.insider.retrofit.model.SignUpPostReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SellerSignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySellerSignUpBinding
    private lateinit var viewModel : SignUpViewModel
    private lateinit var getSearchResult : ActivityResultLauncher<Intent>
    private val userAPI = RetrofitInstance.getInstance().create(UserInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_sign_up)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        binding.vm = viewModel
        binding.lifecycleOwner = this

        getSearchResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val fulladdress = it.data?.getStringExtra("data")
            if (!fulladdress.isNullOrBlank()){
                val addressSplit = fulladdress.split(",")
                binding.addressText.text = addressSplit[0]
                binding.addressNum.text = addressSplit[1]
            }
        }

        initView()
    }

    private fun initView(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        with(binding){

            idEdit.addTextChangedListener { viewModel.setUserId(it.toString()) }
            nicknameEdit.addTextChangedListener { viewModel.setUserNickname(it.toString()) }
            pwdEdit.addTextChangedListener { viewModel.setUserPwd(it.toString()) }
            pwdCheckEdit.addTextChangedListener { viewModel.setCheckPwd(it.toString()) }
            emailEdit.addTextChangedListener { viewModel.setUserEmail(it.toString()) }
            registerNumEdit.addTextChangedListener { viewModel.setResgisterNum((it.toString())) }

            addressFindBtn.setOnClickListener {
                val intent = Intent(this@SellerSignUpActivity, AddressActivity::class.java)
                getSearchResult.launch(intent)
            }

            signUpBtn.setOnClickListener {
                lifecycleScope.launch {

                    val userId = idEdit.text.toString()
                    val nickname = nicknameEdit.text.toString()
                    val pwd = pwdEdit.text.toString()
                    val email = emailEdit.text.toString()
                    val zipCode = addressNum.text.toString().toInt()
                    val detailAddress = addressText.text.toString()
                    val registerNum = registerNumEdit.text.toString().toLong()
                    val sellerOrBuyer: Int = 1

                    // 구매자 회원가입
                    val signUpPostReq = SignUpPostReq(userId,nickname,pwd,email, zipCode, detailAddress, sellerOrBuyer, registerNum)

                    val response = withContext(Dispatchers.IO){
                        userAPI.createUser(signUpPostReq)
                    }

                    if (response.isSuccessful){
                        val baseResponse = response.body()
                        if(baseResponse?.isSuccess == true){
                            val loginPostRes = baseResponse.result
                            Toast.makeText(this@SellerSignUpActivity, "회원가입 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            // baseResponse가 실패
                            //Log.d("signuperror",baseResponse!!.message)
                        }
                    }else{
                        // 네트워크 에러 처리
                    }
                }
            }
        }

    }
}