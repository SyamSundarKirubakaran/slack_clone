package com.bugscript.slackup.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bugscript.slackup.Model.Message
import com.bugscript.slackup.R
import com.bugscript.slackup.Services.UserDataService

class MessageAdapter(val context : Context, val messages : ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindMessage(context,messages = messages[p1])
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userImage = itemView.findViewById<ImageView>(R.id.messageUserImage)
        val timeStamp = itemView.findViewById<TextView>(R.id.timeStampLabel)
        val userName = itemView.findViewById<TextView>(R.id.messageUserName)
        val messageBody = itemView.findViewById<TextView>(R.id.messageBodyLabel)

        fun bindMessage(context: Context, messages: Message){
            val resourceId = context.resources.getIdentifier(messages.userAvatar, "drawable",context.packageName)
            userImage.setImageResource(resourceId)
            userImage.setBackgroundColor(UserDataService.returnAvatarColor(messages.userAvatarColor))
            userName.text = messages.userName
            timeStamp.text = messages.timeStamp
            messageBody.text = messages.message
        }

    }

}