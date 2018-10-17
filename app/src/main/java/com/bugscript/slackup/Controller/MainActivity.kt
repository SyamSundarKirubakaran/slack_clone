package com.bugscript.slackup.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bugscript.slackup.R
import com.bugscript.slackup.R.id.drawer_layout
import com.bugscript.slackup.Services.AuthService
import com.bugscript.slackup.Services.UserDataService
import com.bugscript.slackup.Utilities.BROADCAST_USER_DATA_CHNAGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHNAGE))

    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceID = resources.getIdentifier(UserDataService.avatarName,"drawable",packageName)
                userImageNavHeader.setImageResource(resourceID)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginButtonNavHeader.text = "Logout"
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginButtonNavClicked(view : View){
        if(AuthService.isLoggedIn){
            UserDataService.logout()
            userNameNavHeader.text = "Name"
            userEmailNavHeader.text = "Email"
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginButtonNavHeader.text = "LOGIN"
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun addChannelButtonClicked(view : View){
        if(AuthService.isLoggedIn) {
            var builder = AlertDialog.Builder(this)
            var dialogueView = layoutInflater.inflate(R.layout.add_channel_dialogue, null)
            builder.setView(dialogueView)
                    .setPositiveButton("Add"){ dialogInterface, i ->
                        val nameTextField = dialogueView.findViewById<EditText>(R.id.addChannelNameText)
                        val descTextField = dialogueView.findViewById<EditText>(R.id.addChannelDescText)
                        val channelname = nameTextField.text.toString()
                        val channelDesc = descTextField.text.toString()


                    }
                    .setNegativeButton("Cancel"){ dialogInterface, i ->

                    }
                    .show()
        }
    }

    fun sendMessageButtonClicked(view: View){

    }

}
