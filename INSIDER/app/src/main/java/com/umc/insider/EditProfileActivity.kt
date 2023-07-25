package com.umc.insider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.umc.insider.auth.login.LogInActivity
import com.umc.insider.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)


        binding.logoutTextView.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
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
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

    }
}