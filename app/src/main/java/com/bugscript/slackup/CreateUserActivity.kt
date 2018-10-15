package com.bugscript.slackup

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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
        avatarColor = "[$savedR,$savedG,$savedB,1]"
    }

    fun createCreateUserButtonClicked(view: View){
        
    }
}
