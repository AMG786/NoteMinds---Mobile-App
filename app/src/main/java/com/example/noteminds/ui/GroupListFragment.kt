package com.example.noteminds.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.MainActivity
import com.example.noteminds.databinding.FragmentGroupListBinding
import com.example.noteminds.ui.adapter.GroupAdapter
import com.example.noteminds.ui.model.Group
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//class GroupListFragment : Fragment() {
//    private var _binding: FragmentGroupListBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var adapter: GroupAdapter
//    private lateinit var database: FirebaseDatabase
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentGroupListBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        database = FirebaseDatabase.getInstance()
//        val groupsRef = database.getReference("groups")
//
//        adapter = GroupAdapter(groupsRef) { group ->
//            (activity as MainActivity).replaceFragment(GroupChatFragment.newInstance(group.id))
//        }
//
//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = this@GroupListFragment.adapter
//        }
//
//        binding.btnCreateGroup.setOnClickListener {
//            showCreateGroupDialog()
//        }
//    }
//
//    private fun showCreateGroupDialog() {
//        // Implementation for creating new groups
//    }
//
//    override fun onStart() {
//        super.onStart()
//        adapter.startListening()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        adapter.stopListening()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


class GroupListFragment : Fragment() {
    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GroupAdapter
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("GroupListFragment", "Fragment loaded")
        System.out.println("GroupListFragment Loaded ")
//        binding.recyclerView.visibility = View.GONE
//        binding.btnCreateGroup.text = "RecyclerView is hidden!"
        database = FirebaseDatabase.getInstance()
        val groupsRef = database.getReference("groups")

        adapter = GroupAdapter(groupsRef) { group ->
//            (activity as MainActivity).replaceFragment(GroupChatFragment.newInstance(group.id))
            val groupchatFragment = group.id?.let { GroupChatFragment.newInstance(it) }
            if (groupchatFragment != null) {
                (activity as MainActivity).navigateToFragment(groupchatFragment)
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GroupListFragment.adapter
        }

        binding.btnCreateGroup.setOnClickListener {
            showCreateGroupDialog()
        }
    }

    private fun showCreateGroupDialog() {
        // Implementation for creating new groups
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