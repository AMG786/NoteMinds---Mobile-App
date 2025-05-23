package com.example.noteminds.ui


import android.content.Context
import com.example.noteminds.NavigationListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteminds.MainActivity
import com.example.noteminds.Resource
import com.example.noteminds.TokenManager
import com.example.noteminds.data.api.RetrofitInstance
import com.example.noteminds.data.model.LoginResponse
import com.example.noteminds.data.model.PdfUploadResponse
import com.example.noteminds.data.repository.AuthRepository
import com.example.noteminds.data.room.AppDatabase
import com.example.noteminds.databinding.FragmentLoginBinding
import com.example.noteminds.ui.model.FirebaseUser
import com.example.noteminds.ui.model.Group
import com.example.noteminds.vm.AuthViewModel
import com.google.android.gms.maps.model.Dash
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
Created by Abdul Mueez 04/16/2025
 */


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authApi = RetrofitInstance.api
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        viewModel = AuthViewModel(AuthRepository(authApi, userDao))
        try {
            FirebaseApp.initializeApp(requireContext())
        } catch (e: IllegalStateException) {
            // Already initialized
        }
        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setupClickListeners()
        observeLoginState()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInputs(username, password)) {
                checkUserExists(username,password)
                viewModel.login(username, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            (activity as? NavigationListener)?.navigateToFragment(RegisterFragment())
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.btnLogin.isEnabled = false
                    //binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.btnLogin.isEnabled = true
                    //binding.progressBar.visibility = View.GONE
                    state.data?.let { handleLoginSuccess(it) }
                }
                is Resource.Error -> {
                    binding.btnLogin.isEnabled = true
                    //binding.progressBar.visibility = View.GONE
                    showError(state.message ?: "Login failed")
                }
            }
        }
    }

    private fun handleLoginSuccess(response: LoginResponse) {

        TokenManager.saveToken(requireContext(), response.token)

        val dashboardFragment = DashboardFragment().apply {
            arguments = Bundle().apply {
                putString("userId", response.userId)
                putString("username", response.username)
            }
        }
        // Firebase User for chat functionality
        (activity as? NavigationListener)?.navigateToFragment(dashboardFragment)
    }

    private fun validateInputs(username: String, password: String): Boolean {
        var isValid = true
        if (username.isEmpty()) {
            binding.etUsername.error = "Username required"
            isValid = false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password required"
            isValid = false
        }
        return isValid
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

// Firebase Functionality
private fun checkUserExists(username: String, password: String) {
    val usersRef = database.getReference("users")

    usersRef.orderByChild("username").equalTo(username)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found, check password
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(FirebaseUser::class.java)
                        if (user?.password == password) {
                            // Password matches, login successful
//                            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                            // Navigate to MainActivity
//                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                            intent.putExtra("username", username)
//                            startActivity(intent)
//                            finish()
                            System.out.println("Check -- ")

                            if (user != null) {
                                System.out.println("Check --  in ")
                                (activity as? MainActivity)?.setCurrentUser(user)
                            }



                        } else {
//                            Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    registerUser(username,password)
//                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
//                Toast.makeText(this@LoginActivity, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
}

    private fun registerUser(username: String, password: String) {
        val usersRef = database.getReference("users")

        // First check if username already exists
        usersRef.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
//                        Toast.makeText(this@LoginActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        // Create new user
                        val userId = usersRef.push().key
                        val user = FirebaseUser(userId, username, password)

                        if (userId != null) {
                            usersRef.child(userId).setValue(user)
                                .addOnSuccessListener {
//                                    Toast.makeText(this@LoginActivity, "Registration successful", Toast.LENGTH_SHORT).show()

                                    // Navigate to MainActivity after registration
//                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                    intent.putExtra("username", username)
//                                    startActivity(intent)
//                                    finish()
                                }
                                .addOnFailureListener {
//                                    Toast.makeText(this@LoginActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
//                    Toast.makeText(this@LoginActivity, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


//    private fun checkFirebaseInitialization() {
//        try {
//            val apps = FirebaseApp.getApps(requireContext())
//            if (apps.isEmpty()) {
//                Log.e("Firebase", "Not initialized")
//            } else {
//                Log.d("Firebase", "Initialized successfully")
//            }
//        } catch (e: Exception) {
//            Log.e("Firebase", "Initialization check failed", e)
//        }
//    }
//    private fun initializeFirebase() {
//        try {
//            if (FirebaseApp.getApps(requireContext()).isEmpty()) {
//                FirebaseApp.initializeApp(requireContext())
//            }
//        } catch (e: Exception) {
//            Log.e("FirebaseInit", "Initialization failed", e)
//        }
//    }

//    private fun authenticateUser(username: String, password: String) {
//        initializeFirebase()
//        // Query Firebase to find user with this username
//        FirebaseApp.initializeApp(requireContext())
//        val firebaseApps = FirebaseApp.getApps(requireContext())
//        Log.d("FirebaseDebug", "Initialized apps: ${firebaseApps.size}")
//
//        if (firebaseApps.isEmpty()) {
//            Toast.makeText(requireContext(), "Firebase not initialized!", Toast.LENGTH_LONG).show()
//            return
//        }
//        val usersRef = FirebaseDatabase.getInstance().getReference("users")
//
//        usersRef.orderByChild("username").equalTo(username)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        for (userSnapshot in snapshot.children) {
//                            val user = userSnapshot.getValue(User::class.java)
//                            if (user != null && user.password == password) {
//                                // Success - navigate to chat list
//                                saveUserToPrefs(user)
//                                (activity as? NavigationListener)?.navigateToFragment(PdfUploadFragment())
//                                System.out.println("--lOGIN")
//                            } else {
//                                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    } else {
//                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
//
//    private fun hashPassword(password: String): String {
//        // Implement proper password hashing (e.g., SHA-256)
//        return password.hashCode().toString()
//    }
//
//    private fun saveUserToPrefs(user: User) {
//        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//        prefs.edit().apply {
//            putString("username", user.username)
//            //putString("userId", user.uid)
//            apply()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}