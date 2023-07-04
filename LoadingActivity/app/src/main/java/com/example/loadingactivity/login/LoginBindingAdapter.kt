package com.example.loadingactivity.login

import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.loadingactivity.R


@BindingAdapter("securityState")
fun SecurityState(view : EditText, securityState : SecurityState){

    when(securityState){
        SecurityState.SAFE -> {view.setCompoundDrawablesWithIntrinsicBounds(null,null, getDrawable(view.context,R.drawable.safe),null)}
        SecurityState.NORMAL -> {view.setCompoundDrawablesWithIntrinsicBounds(null,null, getDrawable(view.context,R.drawable.normal),null)}
        SecurityState.DANGER -> {view.setCompoundDrawablesWithIntrinsicBounds(null,null, getDrawable(view.context,R.drawable.danger),null)}
    }
}

@BindingAdapter("stateCheck")
fun StateCheck(view : EditText, editState : EditState){
    when(editState){
        EditState.CHECK -> {view.setCompoundDrawablesWithIntrinsicBounds(null,null, getDrawable(view.context,R.drawable.baseline_check_24),null)}
        EditState.CLOSE -> {view.setCompoundDrawablesWithIntrinsicBounds(null,null, getDrawable(view.context,R.drawable.baseline_close_24),null)}
    }
}