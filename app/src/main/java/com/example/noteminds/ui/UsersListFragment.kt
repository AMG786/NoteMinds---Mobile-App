package com.example.noteminds.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.MainActivity
import com.example.noteminds.databinding.FragmentUsersListBinding
import com.example.noteminds.ui.adapter.UsersAdapter
import com.google.firebase.database.DatabaseReference

class UsersListFragment : Fragment() {
    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!
    private lateinit var usersRef: DatabaseReference
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersRef = (activity as MainActivity).getDatabase().getReference("users")
        adapter = UsersAdapter(usersRef) { userId, username ->
            val chatFragment = ChatFragment.newInstance(userId, username)
            (activity as MainActivity).navigateToFragment(chatFragment)
        }

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter
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