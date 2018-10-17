package com.bugscript.slackup.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bugscript.slackup.R
import com.bugscript.slackup.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginButtonClicked(view : View){
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            AuthService.loginUser(this,email = email,password = password){ loginSuccess ->
                if(loginSuccess){
                    AuthService.findUserByEmail(context = this){userFound ->
                        if(userFound){
                            finish()
                        }

                    }
                }

            }
        }
    }

    fun loginCreateUserButtonClicked(view : View){
        startActivity(Intent(this, CreateUserActivity::class.java))
        finish()
    }
}
