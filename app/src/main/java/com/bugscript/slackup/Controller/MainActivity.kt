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
import android.widget.ArrayAdapter
import android.widget.EditText
import com.bugscript.slackup.Model.Channel
import com.bugscript.slackup.Model.Message
import com.bugscript.slackup.R
import com.bugscript.slackup.R.id.drawer_layout
import com.bugscript.slackup.Services.AuthService
import com.bugscript.slackup.Services.MessageService
import com.bugscript.slackup.Services.UserDataService
import com.bugscript.slackup.Utilities.BROADCAST_USER_DATA_CHNAGE
import com.bugscript.slackup.Utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter: ArrayAdapter<Channel>
    var selectedChannel: Channel? = null

    private fun setupAdapter(){
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,MessageService.channels)
        channel_list.adapter = channelAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        socket.connect()
        socket.on("channelCreated",onNewChannel)
        socket.on("messageCreated", onNewMessage)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setupAdapter()

        channel_list.setOnItemClickListener { _, _, i, _ ->
            selectedChannel = MessageService.channels[i]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }

        if(App.prefs.isLoggedIn){
            AuthService.findUserByEmail(this){}
        }

    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHNAGE))
        super.onResume()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        socket.disconnect()
        super.onDestroy()
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, p1: Intent?) {
            if (App.prefs.isLoggedIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceID = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceID)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginButtonNavHeader.text = "Logout"

                MessageService.getChannel {complete ->
                    if(complete){
                        if(MessageService.channels.count() > 0){
                            selectedChannel = MessageService.channels[0]
                            channelAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    fun updateWithChannel(){
        mainChannelName.text = "#${selectedChannel?.name}"
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginButtonNavClicked(view : View){
        if(App.prefs.isLoggedIn){
            UserDataService.logout()
            userNameNavHeader.text = "Name"
            userEmailNavHeader.text = "Email"
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginButtonNavHeader.text = "LOGIN"
            MessageService.channels.clear()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun addChannelButtonClicked(view : View){
        if(App.prefs.isLoggedIn) {
            var builder = AlertDialog.Builder(this)
            var dialogueView = layoutInflater.inflate(R.layout.add_channel_dialogue, null)
            builder.setView(dialogueView)
                    .setPositiveButton("Add"){ _, _ ->
                        val nameTextField = dialogueView.findViewById<EditText>(R.id.addChannelNameText)
                        val descTextField = dialogueView.findViewById<EditText>(R.id.addChannelDescText)
                        val channelname = nameTextField.text.toString()
                        val channelDesc = descTextField.text.toString()

                        socket.emit("newChannel", channelname,channelDesc)

                    }
                    .setNegativeButton("Cancel"){ _, _ ->

                    }
                    .show()
        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        runOnUiThread {
            val channelName = args[0] as String
            val channelDesc = args[1] as String
            val channelID = args[2] as String

            val newChannel = Channel(channelName,channelDesc,channelID)
            MessageService.channels.add(newChannel)
            channelAdapter.notifyDataSetChanged()
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
        runOnUiThread{
            val messageBody = args[0] as String
            val channelId = args[1] as String
            val userName = args[2] as String
            val userAvatar = args[3] as String
            val userAvatarColor = args[4] as String
            val id = args[5] as String
            val timeStamp = args[6] as String

            val newMessage = Message(messageBody,userName,channelId,userAvatar,userAvatarColor,id,timeStamp)
            MessageService.messages.add(newMessage)
            println(newMessage.message)
        }
    }

    fun sendMessageButtonClicked(view: View){
        if(App.prefs.isLoggedIn && messageTextField.text.isNotEmpty() && selectedChannel != null){
            val userId = UserDataService.id
            val channelId = selectedChannel!!.id
            socket.emit("newMessage", messageTextField.text.toString(),userId,channelId,
                    UserDataService.name, UserDataService.avatarName, UserDataService.avatarColor)
            messageTextField.text.clear()
        }
    }

}
