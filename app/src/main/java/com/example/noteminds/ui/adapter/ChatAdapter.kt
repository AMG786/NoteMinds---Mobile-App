package com.example.noteminds.ui.adapter
//
//class ChatAdapter(
//    private val currentUser: String,
//    private val messages: List<ChatMessage>
//) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
//
//    inner class MessageViewHolder(val binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
//        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MessageViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
//        val message = messages[position]
//
//        if (message.sender == currentUser) {
//            // Sent message
//            holder.binding.sentMessageLayout.visibility = View.VISIBLE
//            holder.binding.receivedMessageLayout.visibility = View.GONE
//
//            if (message.type == "text") {
//                holder.binding.sentTextMessage.text = message.message
//                holder.binding.sentTextMessage.visibility = View.VISIBLE
//                holder.binding.sentImageMessage.visibility = View.GONE
//            } else {
//                holder.binding.sentTextMessage.visibility = View.GONE
//                holder.binding.sentImageMessage.visibility = View.VISIBLE
//                Glide.with(holder.itemView.context)
//                    .load(message.message)
//                    .into(holder.binding.sentImageMessage)
//            }
//
//            holder.binding.sentTime.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
//                .format(Date(message.timestamp))
//        } else {
//            // Received message
//            holder.binding.sentMessageLayout.visibility = View.GONE
//            holder.binding.receivedMessageLayout.visibility = View.VISIBLE
//
//            if (message.type == "text") {
//                holder.binding.receivedTextMessage.text = message.message
//                holder.binding.receivedTextMessage.visibility = View.VISIBLE
//                holder.binding.receivedImageMessage.visibility = View.GONE
//            } else {
//                holder.binding.receivedTextMessage.visibility = View.GONE
//                holder.binding.receivedImageMessage.visibility = View.VISIBLE
//                Glide.with(holder.itemView.context)
//                    .load(message.message)
//                    .into(holder.binding.receivedImageMessage)
//            }
//
//            holder.binding.receivedTime.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
//                .format(Date(message.timestamp))
//        }
//    }
//
//    override fun getItemCount() = messages.size
//}