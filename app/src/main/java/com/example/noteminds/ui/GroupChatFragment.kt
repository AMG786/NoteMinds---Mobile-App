package com.example.noteminds.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.MainActivity
import com.example.noteminds.databinding.FragmentGroupChatBinding
import com.example.noteminds.ui.adapter.GroupMessageAdapter
import com.example.noteminds.ui.model.Group
import com.example.noteminds.ui.model.GroupMessage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupChatFragment : Fragment() {
    private var _binding: FragmentGroupChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GroupMessageAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var groupId: String
    private lateinit var currentUserId: String

    companion object {
        private const val ARG_GROUP_ID = "groupId"

        fun newInstance(groupId: String): GroupChatFragment {
            val fragment = GroupChatFragment()
            val args = Bundle()
            args.putString(ARG_GROUP_ID, groupId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupId = arguments?.getString(ARG_GROUP_ID) ?: return
        currentUserId = (activity as MainActivity).getCurrentUserId()
        database = FirebaseDatabase.getInstance()

        setupRecyclerView()
        loadGroupInfo()

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecyclerView() {
        val messagesRef = database.getReference("groups/$groupId/messages")

        adapter = GroupMessageAdapter(currentUserId, messagesRef)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GroupChatFragment.adapter
        }
    }

    private fun loadGroupInfo() {
        database.getReference("groups/$groupId").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val group = snapshot.getValue(Group::class.java)
                    binding.tvGroupName.text = group?.name
                    binding.tvGroupDescription.text = group?.description
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to load group info", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString().trim()
        if (messageText.isEmpty()) return

        val currentUser = (activity as MainActivity).getCurrentUserId()
        val message = GroupMessage(
            text = messageText,
            senderId = (activity as MainActivity).currentUser1?.id ?: "",
            senderName = (activity as MainActivity).currentUser1?.username ?: "Anonymous",
            timestamp = System.currentTimeMillis()
        )

        database.getReference("groups/$groupId/messages").push()
            .setValue(message)
            .addOnSuccessListener {
                binding.etMessage.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
            }
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