package com.umc.insider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
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
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.UserInterface
import com.umc.insider.retrofit.model.UserPutProfileReq
import com.umc.insider.retrofit.model.UserPutReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var getSearchResult : ActivityResultLauncher<Intent>

    private var imgUri : Uri? = null
    private var password : String = ""

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

        val userIdx = UserManager.getUserIdx(applicationContext)!!.toLong()
        Log.d("EDITTT", "userIdx : {$userIdx}")
        val UserApi = RetrofitInstance.getInstance().create(UserInterface::class.java)


        lifecycleScope.launch{
            try {
                val response = withContext(Dispatchers.IO){
                    UserApi.getUserById(userIdx)
                }
                Log.d("EDITTT", "$response")

                withContext(Dispatchers.Main){
                    binding.nicknameTV.setText(response.nickname)
                    binding.idTextView.text = response.userId
                    binding.emailTextView.setText(response.email)
                    binding.editaddressText.text = response.detailAddress
                    binding.editaddressNum.setText(response.zipCode.toString())
                    password = response.pw
                    Log.d("EDITTT", "password : $password")

                    if(response.img != null) {
                        Glide.with(binding.profileImg.context)
                            .load(response.img)
                            .placeholder(android.R.color.transparent)
                            .into(binding.profileImg)
                    } else {

                    }
                }

            }catch( e : Exception){
                Log.e("EDITTT", "$e")
            }
        }



        // 정보 저장
        binding.infoSaveBtn.setOnClickListener {

            Log.d("EDITTT", "확인 전")

            if(binding.nicknameTV.text.isNullOrBlank() || binding.emailTextView.text.isNullOrBlank()){
                Toast.makeText(this@EditProfileActivity, "빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("EDITTT", "데이터 넣기 전")

            val putUserReq = UserPutReq(
                id = userIdx,
                userId = binding.idTextView.text.toString(),
                nickname = binding.nicknameTV.text.toString(),
                pw = password,
                email = binding.emailTextView.text.toString(),
                zipCode = binding.editaddressNum.text.toString().toInt(),
                detailAddress = binding.editaddressText.text.toString()
            )

            Log.d("EDITTT", "성공 직전")

            lifecycleScope.launch{
                try{
                    val response = UserApi.modifyUser(putUserReq)
                    Log.d("EDITTT", "${response.isSuccess}")
                    Toast.makeText(applicationContext, "수정을 완료하였습니다!", Toast.LENGTH_SHORT)

                }catch(e:Exception){
                    Log.e("EDITTT", "$e")
                }
            }

            // 이미지 수정
            lifecycleScope.launch {
                val putUserProfileReq = UserPutProfileReq(id = userIdx)

                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFileName = "User_${userIdx}_${timeStamp}"

                if (imgUri != null) {
                    val imageFile = convertImageUriToPngFile(applicationContext, imageFileName)
                    val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile!!)
                    val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
                    try {
                        val response = UserApi.registerProfile(putUserProfileReq, imagePart)
                        Log.d("EDITTT", "${response.isSuccess}")

                    } catch (e: Exception) {
                        Log.e("EDITTT", "$e")
                    }
                } else {
                    Log.d("EDITTT", "이미지 파일이 존재하지 않습니다.")
                }

            }

            finish()
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

    override fun onDestroy() {
        super.onDestroy()

    }

}