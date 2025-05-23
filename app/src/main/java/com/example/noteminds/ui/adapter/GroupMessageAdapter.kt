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
import com.example.noteminds.R
import com.example.noteminds.ui.model.GroupMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*

class GroupMessageAdapter(
    private val currentUserId: String,
    ref: DatabaseReference
) : FirebaseRecyclerAdapter<GroupMessage, GroupMessageAdapter.MessageViewHolder>(
    FirebaseRecyclerOptions.Builder<GroupMessage>()
        .setQuery(ref.orderByChild("timestamp"), GroupMessage::class.java)
        .build()
) {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer)
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvSender: TextView = itemView.findViewById(R.id.tvSender)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)

        fun bind(message: GroupMessage) {
            tvMessage.text = message.text
            tvSender.text = message.senderName
            tvTimestamp.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Date(message.timestamp))

            // Set different styling for current user's messages
            if (message.senderId == currentUserId) {
                // Current user's message - align right
                val params = messageContainer.layoutParams as LinearLayout.LayoutParams
                params.gravity = Gravity.END
                messageContainer.layoutParams = params

                messageContainer.setBackgroundResource(R.drawable.bg_message_sent)
                tvMessage.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                tvSender.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                tvTimestamp.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            } else {
                // Other user's message - align left
                val params = messageContainer.layoutParams as LinearLayout.LayoutParams
                params.gravity = Gravity.START
                messageContainer.layoutParams = params

                messageContainer.setBackgroundResource(R.drawable.bg_message_received)
                tvMessage.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                tvSender.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                tvTimestamp.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: GroupMessage) {
        holder.bind(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        // Optional: Add any behavior when data changes (like scrolling to bottom)
    }
}