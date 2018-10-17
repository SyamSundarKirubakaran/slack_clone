package com.bugscript.slackup.Services

import android.graphics.Color
import com.bugscript.slackup.Controller.App
import java.util.*

object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout(){
        id = ""
        avatarName = ""
        avatarColor = ""
        email = ""
        name = ""
        App.prefs.isLoggedIn = false
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
    }

    fun returnAvatarColor(components : String): Int{
        // [0.5,0.5,0.5,1]
        // 0.5 0.5 0.5 1
        val strippedColor = components
                .replace("[","")
                .replace("]","")
                .replace(",","")
        var r=0
        var g=0
        var b=0

        val scanner = Scanner(strippedColor)
        if(scanner.hasNext()){
            r= (scanner.nextDouble() * 255).toInt()
            g= (scanner.nextDouble() * 255).toInt()
            b= (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r,g,b)
    }
}