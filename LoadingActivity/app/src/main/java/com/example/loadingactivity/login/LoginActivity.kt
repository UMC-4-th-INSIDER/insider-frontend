package com.example.loadingactivity.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.loadingactivity.R
import com.example.loadingactivity.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        initView()

        binding.vm = viewModel
        binding.lifecycleOwner = this


    }

    private fun initView(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        with(binding){
            idEdit.addTextChangedListener {
                viewModel.setUserId(it.toString())
            }
            pwdEdit.addTextChangedListener {
                viewModel.setUserPwd(it.toString())
            }
            emailEdit.addTextChangedListener {
                viewModel.setUserEmail(it.toString())
            }

        }
        with(binding){

            idEdit.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when{
                        count > 0 -> idEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(pwdEdit.context,R.drawable.baseline_check_24), null)
                        else -> idEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(pwdEdit.context,R.drawable.baseline_close_24), null)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            pwdEdit.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when{
                        count > 7 -> pwdEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(pwdEdit.context,R.drawable.safe), null)
                        count > 4 -> pwdEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(pwdEdit.context,R.drawable.normal), null)
                        else ->  pwdEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(pwdEdit.context,R.drawable.danger), null)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            emailEdit.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when{
                        count > 0 -> emailEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(pwdEdit.context,R.drawable.baseline_check_24), null)
                        else -> emailEdit.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(pwdEdit.context,R.drawable.baseline_close_24), null)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            })
        }
    }
}