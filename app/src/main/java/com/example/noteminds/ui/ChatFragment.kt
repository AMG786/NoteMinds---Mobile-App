package com.example.noteminds.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.MainActivity
import com.example.noteminds.databinding.FragmentChatBinding
import com.example.noteminds.ui.adapter.MessagesAdapter
import com.example.noteminds.ui.model.Message
import com.google.firebase.database.DatabaseReference
import java.util.*

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var messagesRef: DatabaseReference
    private lateinit var currentUserId: String
    private lateinit var receiverUserId: String
    private lateinit var receiverUsername: String
    private lateinit var adapter: MessagesAdapter

    companion object {
        private const val ARG_USER_ID = "userId"
        private const val ARG_USERNAME = "username"

        fun newInstance(userId: String, username: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = (activity as MainActivity).getCurrentUserId()
        receiverUserId = arguments?.getString(ARG_USER_ID) ?: ""
        receiverUsername = arguments?.getString(ARG_USERNAME) ?: ""

        binding.tvChatWith.text = "Chat with $receiverUsername"

        val chatId = if (currentUserId < receiverUserId) {
            "${currentUserId}_$receiverUserId"
        } else {
            "${receiverUserId}_$currentUserId"
        }

        messagesRef = (activity as MainActivity).getDatabase()
            .getReference("chats/$chatId/messages")

        adapter = MessagesAdapter(messagesRef, currentUserId)

        binding.messagesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.messagesRecyclerView.adapter = adapter

        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.etMessage.text.clear()
            }
        }
    }

    private fun sendMessage(messageText: String) {
        val message = Message(
            text = messageText,
            senderId = currentUserId,
            receiverId = receiverUserId,
            timestamp = System.currentTimeMillis()
        )

        messagesRef.push().setValue(message)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}