package com.example.loadingactivity.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _userId = MutableLiveData("")
    val userId : LiveData<String> = _userId

    private val _userPWD = MutableLiveData("")
    val userPWD : LiveData<String> = _userPWD

    private val _userEmail = MutableLiveData("")
    val userEmail : LiveData<String> = _userEmail

    val securityState : LiveData<SecurityState> = Transformations.map(_userPWD){
        when{
            it.length > 7 -> { SecurityState.SAFE }
            it.length > 4 -> { SecurityState.NORMAL }
            else -> {SecurityState.DANGER}
        }
    }

    val idState : LiveData<EditState> = Transformations.map(_userId){
        when{
            it.length > 1 -> { EditState.CHECK }
            else -> { EditState.CLOSE}
        }
    }

    val emailState : LiveData<EditState> = Transformations.map(_userEmail){
        when{
            it.length > 1 -> { EditState.CHECK }
            else -> { EditState.CLOSE}
        }
    }

    fun setUserId(id : String){
        _userId.value = id
    }

    fun setUserPwd(pwd : String){
        _userPWD.value = pwd
    }

    fun setUserEmail(email : String){
        _userEmail.value = email
    }


}

enum class EditState{
    CHECK,
    CLOSE
}

enum class SecurityState{
    SAFE,
    NORMAL,
    DANGER
}