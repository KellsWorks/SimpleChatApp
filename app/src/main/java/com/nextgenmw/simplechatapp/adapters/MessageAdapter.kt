package com.nextgenmw.simplechatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nextgenmw.simplechatapp.App
import com.nextgenmw.simplechatapp.R
import com.nextgenmw.simplechatapp.models.Message
import com.nextgenmw.simplechatapp.utils.DateUtils

private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2

class MessageAdapter (val context: Context) : RecyclerView.Adapter<MessageViewHolder>() {

    private val messages: ArrayList<Message> = ArrayList()

    fun addMessage(message: Message){
        messages.add(message)
        notifyDataSetChanged()
    }

    inner class MyMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.txtMyMessage)
        private var timeText: TextView = view.findViewById(R.id.txtMyMessageTime)

        override fun bind(message: Message) {
            messageText.text = message.message
            timeText.text = DateUtils.fromMillisToTimeString(message.time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.message, parent, false)
            )
        } else {
            OtherMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.other_message, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if(App.user == message.user) {
            VIEW_TYPE_MY_MESSAGE
        }
        else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    inner class OtherMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.txtOtherMessage)
        private var userText: TextView = view.findViewById(R.id.txtOtherUser)
        private var timeText: TextView = view.findViewById(R.id.txtOtherMessageTime)

        override fun bind(message: Message) {
            messageText.text = message.message
            userText.text = message.user
            timeText.text = DateUtils.fromMillisToTimeString(message.time)
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.bind(message)
    }
}

open class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message:Message) {}
}