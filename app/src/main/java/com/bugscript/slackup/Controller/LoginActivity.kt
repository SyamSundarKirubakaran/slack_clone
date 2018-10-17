package com.bugscript.slackup.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bugscript.slackup.R
import com.bugscript.slackup.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        enableSpinner(false)
    }

    fun loginLoginButtonClicked(view : View){
        enableSpinner(true)
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyBoard()
        if (email.isNotEmpty() && password.isNotEmpty()){
            AuthService.loginUser(this,email = email,password = password){ loginSuccess ->
                if(loginSuccess){
                    AuthService.findUserByEmail(context = this){userFound ->
                        if(userFound){
                            finish()
                        }
                    }
                } else {
                    enableSpinner(false)
                    errorToast()
                }
            }
        }else{
            errorToast()
            enableSpinner(false)
        }
    }

    fun loginCreateUserButtonClicked(view : View){
        startActivity(Intent(this, CreateUserActivity::class.java))
        finish()
    }

    fun errorToast(){
        Toast.makeText(this, "Something went wrong, please try again.!",Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable : Boolean){
        if(enable){
            loginSpinner.visibility = View.VISIBLE
        }else{
            loginSpinner.visibility = View.INVISIBLE
        }
        loginCreateUserButton.isEnabled = !enable
        loginLoginButton.isEnabled = !enable

    }

    fun hideKeyBoard(){
        val inputManager =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }
}
