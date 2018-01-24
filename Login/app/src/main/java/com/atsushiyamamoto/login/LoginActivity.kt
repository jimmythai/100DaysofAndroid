package com.atsushiyamamoto.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.login_activity.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager


class LoginActivity: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginButtonLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == loginButtonLogin) {
            val messages = mutableListOf<String>()
            val isEmailBlank = loginEditTextEmail.text.isBlank()
            val isPasswordBlank = loginEditTextPassword.text.isBlank()

            if (isEmailBlank) {
                messages.add("Fill out the email text field.")
            }

            if (isPasswordBlank) {
                messages.add("Fill out the password text field.")
            }

            if (!isEmailBlank && !isPasswordBlank) {
                messages.add("Success! Your email is ${loginEditTextEmail.text}")
            }

            val message = messages.joinToString("\n")

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        hideKeyboard()
        return super.onTouchEvent(event)
    }
}

fun LoginActivity.hideKeyboard() {
    val view = currentFocus

    if (view == null) {  return  }

    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}