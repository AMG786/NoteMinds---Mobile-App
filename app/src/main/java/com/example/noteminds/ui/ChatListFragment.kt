package com.example.noteminds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteminds.R

// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"


/**
Created by Abdul Mueez 04/16/2025
*/

//class ChatListFragment : Fragment() {
//    private lateinit var binding: FragmentChatListBinding
//    private lateinit var chatListAdapter: ChatListAdapter
//    private val chatList = mutableListOf<ChatListItem>()
//    private var currentUser: String = ""
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentChatListBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        currentUser = getCurrentUserId()
//        setupRecyclerView()
//        fetchChatList()
//    }
//
//    private fun getCurrentUserId(): String {
//        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//        return prefs.getString("userId", "") ?: ""
//    }
//
//    private fun setupRecyclerView() {
//        chatListAdapter = ChatListAdapter(chatList) { chatId, withUser ->
//            val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(chatId, withUser)
//            findNavController().navigate(action)
//        }
//        binding.chatListRecyclerView.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = chatListAdapter
//        }
//    }
//
//    private fun fetchChatList() {
//        val userChatsRef = FirebaseDatabase.getInstance().getReference("user_chats/$currentUser")
//
//        userChatsRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                chatList.clear()
//                for (chatSnapshot in snapshot.children) {
//                    val chat = chatSnapshot.getValue(ChatListItem::class.java)
//                    chat?.let {
//                        chatList.add(it)
//                    }
//                }
//                chatListAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(), "Failed to load chats", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}