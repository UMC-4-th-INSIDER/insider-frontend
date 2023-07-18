package com.umc.insider.auth.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    private val _userId = MutableLiveData("")
    val userId : LiveData<String> = _userId

    private val _userNickname = MutableLiveData("")
    val userNickname : LiveData<String> = _userNickname

    private val _userPWD = MutableLiveData("")
    val userPWD : LiveData<String> = _userPWD

    private val _checkPWD = MutableLiveData("")
    val checkPWD : LiveData<String> = _checkPWD

    private val _userEmail = MutableLiveData("")
    val userEmail : LiveData<String> = _userEmail

    val idState: LiveData<EditState> = Transformations.map(_userId) { id ->
        when {
            id.isNullOrBlank() -> EditState.EMPTY
            REGEX_ID.toRegex().matches(id) -> EditState.CHECK
            else -> EditState.CLOSE
        }
    }

    val nicknameState : LiveData<EditState> = Transformations.map(_userNickname) { nickname ->
        when{
            nickname.isNullOrBlank() -> EditState.EMPTY
            REGEX_NICKNAME.toRegex().matches(nickname) -> EditState.CHECK
            else -> EditState.CLOSE
        }
    }

    val securityState: LiveData<SecurityState> = Transformations.map(_userPWD) { password ->
        when {
            password.length > 7 -> SecurityState.SAFE
            password.length > 4 -> SecurityState.NORMAL
            password.isNotEmpty() -> SecurityState.DANGER
            else -> SecurityState.EMPTY
        }
    }

    val checkState : LiveData<EditState> = Transformations.map(_checkPWD) { checkPassword ->
        when {
            checkPassword.isNullOrBlank() -> EditState.EMPTY
            checkPassword == _userPWD.value -> EditState.CHECK
            else -> EditState.CLOSE
        }
    }

    val emailState: LiveData<EditState> = Transformations.map(_userEmail) { email ->
        when {
            email.isNullOrBlank() -> EditState.EMPTY
            REGEX_EMAIL.toRegex().matches(email) -> EditState.CHECK
            else -> EditState.CLOSE
        }
    }

    fun setUserId(id : String){
        _userId.value = id
    }

    fun setUserNickname(nickname : String){
        _userNickname.value = nickname
    }

    fun setUserPwd(pwd : String){
        _userPWD.value = pwd
    }

    fun setCheckPwd(checkPWD : String){
        _checkPWD.value = checkPWD
    }


    fun setUserEmail(email : String){
        _userEmail.value = email
    }

    companion object{
        //최소 6자 이상, 10자 이하
        //알파벳 소문자와 대문자 포함
        //특수 문자 사용 불가
        private const val REGEX_ID = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{6,10}\$"

        //닉네임은 한글, 영문자, 숫자, 언더스코어(_)만 포함
        //닉네임의 길이는 2자 이상 10자 이하
        private const val REGEX_NICKNAME = "^[가-힣a-zA-Z0-9_]{2,10}\$"

        private const val REGEX_EMAIL = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"
    }
}

enum class EditState{
    CHECK,
    CLOSE,
    EMPTY
}

enum class SecurityState{
    SAFE,
    NORMAL,
    DANGER,
    EMPTY
}