package com.bugscript.slackup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginButtonClicked(view : View){

    }

    fun loginCreateUserButtonClicked(view : View){
        startActivity(Intent(this,CreateUserActivity::class.java))
    }
}
