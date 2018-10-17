package com.bugscript.slackup.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.bugscript.slackup.R
import com.bugscript.slackup.Services.AuthService
import com.bugscript.slackup.Utilities.BROADCAST_USER_DATA_CHNAGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        userAvatar = when(color){
            0 -> "light${avatar}"
            1 -> "dark${avatar}"
            else -> "profileDefault"
        }
        val resourceId = resources.getIdentifier(userAvatar,"drawable",packageName)
        createAvatorImageView.setImageResource(resourceId)
    }

    fun generateBackgrounColor(view: View){
        val random = Random()
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        createAvatorImageView.setBackgroundColor(Color.rgb(red,green,blue))
        //to obtain the decimal equilvalent from 0 to 1
        val savedR = red.toDouble()/255
        val savedG = green.toDouble()/255
        val savedB = blue.toDouble()/255
        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createCreateUserButtonClicked(view: View){
        val userName = createUsernameText.text.toString()
        val password = createPasswordText.text.toString()
        val email = createEmailText.text.toString()

        enableSpinner(true)
        if(userName.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            AuthService.regsiterUser(context = this, email = email, password = password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {

                                    // Local Broadcast
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHNAGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    Toast.makeText(this, "Creation Success", Toast.LENGTH_LONG).show()
                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                }
            }
        }else{
            errorToast()
            enableSpinner(false)
        }
    }

    fun errorToast(){
        Toast.makeText(this, "Something went wrong, please try again.!",Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable : Boolean){
        if(enable){
            createSpinner.visibility = View.VISIBLE
        }else{
            createSpinner.visibility = View.INVISIBLE
        }
        createCreateUserButton.isEnabled = !enable
        createAvatorImageView.isEnabled = !enable
        createBackgroundColorButton.isEnabled = !enable
    }
}
