package com.umc.insider.auth.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.umc.insider.MainActivity
import com.umc.insider.R
import com.umc.insider.auth.AutoLoginManager
import com.umc.insider.auth.TokenManager
import com.umc.insider.auth.signUp.SignUpActivity
import com.umc.insider.databinding.ActivityLogInBinding
import com.umc.insider.retrofit.RetrofitInstance
import com.umc.insider.retrofit.api.UserInterface
import com.umc.insider.retrofit.model.LoginPostReq
import kotlinx.coroutines.launch


class LogInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLogInBinding
    private val userAPI = RetrofitInstance.getInstance().create(UserInterface::class.java)
    private lateinit var autoLoginManager: AutoLoginManager

    // Google
    lateinit var mGoogleSignClient : GoogleSignInClient
    lateinit var resultLauncher : ActivityResultLauncher<Intent>

    // Kakao
    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("kakao", "로그인 실패 $error")
            //Toast.makeText(this@LogInActivity, "어플 없어서 웹으로 실패", Toast.LENGTH_SHORT).show()

        } else if (token != null) {
            Log.d("kakao", "로그인 성공 ${token.accessToken}")
            //Toast.makeText(this@LogInActivity, "어플 없어서 웹으로 성공", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        // Google
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.let {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
            goMainActivity()
        } ?: {}

        // Kakao
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error == null) {
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
                    goMainActivity()
                }
            }
        }

        if(autoLoginManager.isAutoLogin()){
            goMainActivity()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        //Log.d("login", "keyhash : ${Utility.getKeyHash(this)}")

        autoLoginManager = AutoLoginManager(this)

        initView()
        setResultSignUp()

        // Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(this)

        // kakao - 카카오톡이 있으면 카카오톡 로그인, 없으면 카카오 이메일 로그인
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
    }

    private fun initView(){
        with(binding){

            logIn.setOnClickListener {

                if (idEdit.text.isNullOrBlank()) {
                    Toast.makeText(this@LogInActivity, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (pwdEdit.text.isNullOrBlank()) {
                    Toast.makeText(this@LogInActivity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val id = idEdit.text.toString()
                val pwd = pwdEdit.text.toString()
                val loginPostReq = LoginPostReq(id,pwd)

                lifecycleScope.launch {
                    val response = userAPI.logIn(loginPostReq)

                    if(response.isSuccessful){

                        val baseResponse = response.body()

                        if(baseResponse?.isSuccess == true){

                            val loginPostRes = baseResponse.result
                            TokenManager.saveToken(this@LogInActivity, loginPostRes?.jwt)

                            if (autoLoginSwitch.isChecked){
                                autoLoginManager.setAutoLogin(true)
                            }else{
                                autoLoginManager.setAutoLogin(false)
                            }

                            goMainActivity()
                        }else{
                            // baseResponse가 실패한 경우의 처리
                            //Log.d("loginerror",baseResponse!!.message)
                        }

                    }else{
                        // 네트워크 에러 처리
                    }
                }
            }

            singUp.setOnClickListener {
                startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
            }
            googleBtn.setOnClickListener {
                signIn()
            }

            kakaoBtn.setOnClickListener {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LogInActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(this@LogInActivity) { token, error ->
                        if (error != null) {
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            } else {
                                UserApiClient.instance.loginWithKakaoAccount(this@LogInActivity, callback = mCallback)
                            }
                        } else if (token != null) {
                            // token.accessToken}
                            //Toast.makeText(this@LogInActivity, "어플로 성공", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this@LogInActivity, callback = mCallback)
                }
            }
        }
    }

    private fun setResultSignUp() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(
        )) { result ->
            if( result.resultCode == Activity.RESULT_OK){
                val task : Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                //Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
//            Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
//            val familyName = account?.familyName.toString()
//            val givenName = account?.givenName.toString()
//            val displayName = account?.displayName.toString()
//            val photoUrl = account?.photoUrl.toString()
        }catch (e : ApiException){

        }
    }

    private fun signIn() {
        val signInIntent : Intent = mGoogleSignClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun goMainActivity(){
        startActivity(Intent(this@LogInActivity, MainActivity::class.java))
        finish()
    }

}