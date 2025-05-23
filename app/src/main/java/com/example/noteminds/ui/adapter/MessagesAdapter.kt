package com.example.noteminds.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteminds.ui.model.Message
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*
import com.example.noteminds.R

class MessagesAdapter(
    ref: DatabaseReference,
    private val currentUserId: String
) : FirebaseRecyclerAdapter<Message, MessagesAdapter.MessageViewHolder>(
    FirebaseRecyclerOptions.Builder<Message>()
        .setQuery(ref, Message::class.java)
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
        holder.bind(model, currentUserId)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer)

        fun bind(message: Message, currentUserId: String) {
            tvMessage.text = message.text
            tvTimestamp.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Date(message.timestamp))

            val params = itemView.layoutParams as ViewGroup.MarginLayoutParams
            val params1 = messageContainer.layoutParams as LinearLayout.LayoutParams

            if (message.senderId == currentUserId) {
                // Sent message - align right with purple background
                params.marginStart = 300
                params.marginEnd = 16

                itemView.findViewById<LinearLayout>(R.id.messageContainer).background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_message_sent)
                tvMessage.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                params1.gravity = Gravity.END
                //messageContainer.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_message_sent)
            } else {
                // Received message - align left with teal background
                params.marginStart = 16
                params.marginEnd = 100
                itemView.findViewById<LinearLayout>(R.id.messageContainer).background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_message_received)
                tvMessage.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                params1.gravity = Gravity.START
            }
            itemView.layoutParams = params
        }
    }
}