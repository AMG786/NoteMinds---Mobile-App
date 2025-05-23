package com.example.noteminds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.MainActivity
import com.example.noteminds.NavigationListener
import com.example.noteminds.R
import com.example.noteminds.databinding.FragmentAllchatBinding
import com.example.noteminds.databinding.FragmentChatBinding
import com.example.noteminds.ui.adapter.MessagesAdapter
import com.example.noteminds.ui.model.Message
import com.google.firebase.database.DatabaseReference

class AllchatFragment : Fragment() {
    private var _binding: FragmentAllchatBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllchatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardViewTotalQs.setOnClickListener {
            val userListFragment = UsersListFragment()
            (activity as? NavigationListener)?.navigateToFragment(userListFragment)
        }
        binding.cardViewCorrectAns.setOnClickListener {
            val groupListFragment = GroupListFragment()
            (activity as? NavigationListener)?.navigateToFragment(groupListFragment)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}