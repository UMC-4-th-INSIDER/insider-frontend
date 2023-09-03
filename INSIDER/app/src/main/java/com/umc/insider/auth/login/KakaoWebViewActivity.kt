package com.umc.insider.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.umc.insider.R
import com.umc.insider.databinding.ActivityKakaoWebViewBinding

class KakaoWebViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityKakaoWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_kakao_web_view)

        binding.webview.settings.javaScriptEnabled = true

        binding.webview.webViewClient = object  : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request?.url.toString().startsWith("http://localhost:8080/oauth2/callback/kakao")){

                    setResult(101, intent)
                    finish()
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        binding.webview.loadUrl("https://kauth.kakao.com/oauth/authorize?client_id=6026319034992a6bec505b8b721969bb&redirect_uri=http://localhost:8080/oauth2/callback/kakao&response_type=code&client_secret=AkN4ChNdEUztQbawEcWzQyXN1beyANQM")

    }
}