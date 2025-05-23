package com.example.noteminds.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.NavigationListener
import com.example.noteminds.R
import com.example.noteminds.Resource
import com.example.noteminds.TokenManager
import com.example.noteminds.data.api.RetrofitInstance
import com.example.noteminds.data.repository.InterestsRepository
import com.example.noteminds.ui.model.Task
import com.example.noteminds.ui.adapter.TaskAdapter
import com.example.noteminds.data.repository.UserRepository
import com.example.noteminds.data.room.AppDatabase
import com.example.noteminds.vm.DashboardViewModel1
import com.example.noteminds.databinding.FragmentDashboardBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
Created by Abdul Mueez 04/16/2025
 */


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private val tasksList = mutableListOf<Task>()
    private lateinit var userRepository: UserRepository
    private var userId: Int = -1
    private lateinit var authToken: String
    private lateinit var viewModel: DashboardViewModel1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("userId", -1) ?: -1
        authToken = TokenManager.getToken(requireContext()) ?: ""

        if (userId == -1) {
//            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            //(activity as? NavigationListener)?.navigateToFragment(LoginFragment())
            //return
        }

        userRepository = UserRepository(AppDatabase.getDatabase(requireContext()).userDao())
// Initialize ViewModel
        val userRepo = UserRepository(AppDatabase.getDatabase(requireContext()).userDao())
        val interestsRepo = InterestsRepository(
            RetrofitInstance.api,
            AppDatabase.getDatabase(requireContext()).userDao()
        )
        binding.cardViewIncorrectAns.setOnClickListener{
            (activity as? NavigationListener)?.navigateToFragment(PdfUploadFragment())
        }
        viewModel = DashboardViewModel1(userRepo, interestsRepo)
        setupRecyclerView()
        setupObservers()
        loadData()
       // setupProfileNavigation()
//        loadUserData()
//        loadTaskData()
//        setupRecyclerView()
     navigate_to_profile(userId)
    }
    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(mutableListOf()) { task ->
            val args = Bundle().apply {
                putString("topic", task.topic)
            }
            (activity as? NavigationListener)?.navigateToFragment(
                QuizFragment().apply { arguments = args }
            )
        }
        binding.faballchat.setOnClickListener{
            (activity as? NavigationListener)?.navigateToFragment(AllchatFragment())
        }
        binding.rvTasks.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = taskAdapter
        }
    }

    private fun setupObservers() {
        viewModel.tasks.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    resource.data?.let { tasks ->
                        taskAdapter.updateTasks(tasks)
                        updateTaskCount(tasks)
                    }
                }
                is Resource.Error -> showError(resource.message)
            }
        }
    }

    private fun loadData() {
        // Load user data
        lifecycleScope.launch {
            val user = userRepository.getUserById(userId)
            user?.let { binding.tvUsername.text = it.username }
        }

        // Load tasks
        viewModel.loadTasks(userId, authToken)
    }

    private fun updateTaskCount(tasks: List<Task>) {
        val pendingCount = tasks.count { it.status != Task.Status.COMPLETED }
        binding.tvTasksDue.text = if (pendingCount > 0) {
            resources.getQuantityString(
                R.plurals.tasks_due_count,
                pendingCount,
                pendingCount
            )
        } else {
            getString(R.string.no_tasks_due)
        }
    }

    private fun showLoading() {
//        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(message: String?) {
//        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message ?: "Error", Toast.LENGTH_SHORT).show()
    }
    private fun navigate_to_profile(userId: Int){
        binding.lottieAnimationView.setOnClickListener {
            val profileFragment = ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt("userId", userId)
                }
            }
            (activity as? NavigationListener)?.navigateToFragment(profileFragment)
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                user?.let {
                    binding.tvUsername.text = it.username
                }
                updateTaskCountText()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error loading user data", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateTaskCountText() {
        val pendingTasksCount = tasksList.count { it.status != Task.Status.COMPLETED }
        binding.tvTasksDue.text = if (pendingTasksCount > 0) {
            "You have $pendingTasksCount task${if (pendingTasksCount > 1) "s" else ""} due"
        } else {
            "You have two task due"
        }
    }
    private fun loadTaskData() {

        tasksList.clear()

        lifecycleScope.launch {
            try {
                // Fetch user's interests from database
                val interests = userRepository.getUserInterests(userId)
                if (interests.isNotEmpty()) {
                    interests.forEachIndexed { index, topic ->
                        delay(200L)
                        tasksList.add(
                            Task(
                                id = index.toLong(),
                                title = "Quiz on $topic",
                                description = "Test your knowledge about $topic",
                                status = when (index % 3) {
                                    0 -> Task.Status.PENDING
                                    1 -> Task.Status.IN_PROGRESS
                                    else -> Task.Status.COMPLETED
                                },
                                topic = topic,
                                isImportant = index == 0
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("DashboardFragment", "Error loading tasks", e)
                tasksList.clear()
                tasksList.add(
                    Task(
                        id = -1,
                        title = "Error loading tasks",
                        description = "Try again later",
                        status = Task.Status.PENDING,
                        isImportant = true
                    )
                )
            }
            taskAdapter.notifyDataSetChanged()
        }
    }

//    private fun setupRecyclerView() {
//
//        taskAdapter = TaskAdapter(tasksList) { task ->
//            Toast.makeText(requireContext(), "Clicked on: ${task.title}", Toast.LENGTH_SHORT).show()
//            val arg = Bundle().apply {
//                putString("topic", task.topic)
//            }
//            (activity as? NavigationListener)?.navigateToFragment(QuizFragment().apply {
//                arguments = arg
//            })
//        }
//
//        binding.rvTasks.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = taskAdapter
//
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
