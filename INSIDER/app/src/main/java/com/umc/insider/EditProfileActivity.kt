package com.umc.insider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.umc.insider.auth.login.LogInActivity
import com.umc.insider.auth.signUp.AddressActivity
import com.umc.insider.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var getSearchResult : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        binding.lifecycleOwner = this

        getSearchResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val fulladdress = it.data?.getStringExtra("data")
            if (!fulladdress.isNullOrBlank()){
                val addressSplit = fulladdress.split(",")
                binding.editaddressText.text = addressSplit[0]
                binding.editaddressNum.text = addressSplit[1]
            }

        }


        initview()


    }

    private fun initview(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        with(binding){

            addressFindBtn.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, AddressActivity::class.java)
                getSearchResult.launch(intent)
            }

            // 로그아웃 버튼 클릭시
            binding.logoutTextView.setOnClickListener {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(this@EditProfileActivity, gso)
                googleSignInClient.signOut()
                LoginManager.getInstance().logOut()
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        //Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                        return@logout
                    }else {
                        //Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    }
                }
                startActivity(Intent(this@EditProfileActivity, LogInActivity::class.java))
                finish()
            }
        }

    }

}