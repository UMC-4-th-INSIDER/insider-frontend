package com.umc.insider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.umc.insider.auth.TokenManager
import com.umc.insider.auth.UserManager
import com.umc.insider.auth.login.LogInActivity
import com.umc.insider.auth.signUp.AddressActivity
import com.umc.insider.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var getSearchResult : ActivityResultLauncher<Intent>

    private var imgUri : Uri? = null

    private val selectImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imgUri = result.data?.data
            Glide.with(this)
                .load(imgUri)
                .into(binding.profileImg)
        }
    }

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

            if(nicknameTV.text.isNullOrBlank())
                nicknameTV.textSize = 14F
            else
                nicknameTV.textSize = 18F

            // 갤러리 호출
            bringImage.setOnClickListener{
                val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
                selectImageResultLauncher.launch(intent)
            }

            // 주소 수정
            addressFindBtn.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, AddressActivity::class.java)
                getSearchResult.launch(intent)
            }

            // 정보 저장
            infoSaveBtn.setOnClickListener {

                if(nicknameTV.text.isNullOrBlank() || emailTextView.text.isNullOrBlank()){
                    Toast.makeText(this@EditProfileActivity, "빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val resultIntent = Intent()
                resultIntent.putExtra("edited_nickname", nicknameTV.text.toString() + "님")
                resultIntent.putExtra("edited_address", editaddressText.text.toString())
                setResult(RESULT_OK, resultIntent)
                finish()

            }

            // 로그아웃
            logoutTextView.setOnClickListener {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(this@EditProfileActivity, gso)
                googleSignInClient.signOut()
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        //Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                        return@logout
                    }else {
                        //Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    }
                }
                TokenManager.clearToken(this@EditProfileActivity)
                UserManager.clearUserIdx(this@EditProfileActivity)
                startActivity(Intent(this@EditProfileActivity, LogInActivity::class.java))
                finish()
            }
        }

    }

}