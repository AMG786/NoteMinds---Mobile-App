package com.example.noteminds.ui.adapter
//
//class ChatListAdapter(
//    private val chatList: List<ChatListItem>,
//    private val onItemClick: (String, String) -> Unit
//) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
//
//    inner class ViewHolder(val binding: ChatListItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val chat = chatList[position]
//        holder.binding.chatName.text = chat.withUser
//        holder.binding.lastMessage.text = chat.lastMessage
//        holder.binding.timestamp.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
//            .format(Date(chat.timestamp))
//
//        holder.itemView.setOnClickListener {
//            onItemClick(chat.chatId, chat.withUser)
//        }
//    }
//
//    override fun getItemCount() = chatList.size
//}