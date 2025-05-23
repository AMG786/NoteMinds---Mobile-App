package com.example.noteminds.ui

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.noteminds.NavigationListener
import com.example.noteminds.data.api.RetrofitInstance
import com.example.noteminds.data.repository.AuthRepository
import com.example.noteminds.data.repository.UserRepository
import com.example.noteminds.data.room.AppDatabase
import com.example.noteminds.data.room.entities.User
import com.example.noteminds.databinding.FragmentRegisterBinding
import com.example.noteminds.vm.AuthViewModel
import kotlinx.coroutines.launch

/**
 Created by Abdul Mueez 04/16/2025
 */
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: AuthViewModel

    companion object {
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize database and repository
        appDatabase = AppDatabase.getDatabase(requireContext())
        userRepository = UserRepository(appDatabase.userDao())

        viewModel = AuthViewModel(
            repository = AuthRepository(RetrofitInstance.api, appDatabase.userDao())
        )

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnCreateAccount.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }

    }

    private fun validateInputs(): Boolean {
        with(binding) {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val confirmEmail = etConfirmEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            return when {
                username.isEmpty() -> {
                    etUsername.error = "Username required"
                    false
                }
                email.isEmpty() -> {
                    etEmail.error = "Email required"
                    false
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmail.error = "Invalid email format"
                    false
                }
                confirmEmail.isEmpty() -> {
                    etConfirmEmail.error = "Please confirm email"
                    false
                }
                email != confirmEmail -> {
                    etConfirmEmail.error = "Emails don't match"
                    false
                }
                password.isEmpty() -> {
                    etPassword.error = "Password required"
                    false
                }
                password.length < 6 -> {
                    etPassword.error = "Password too short (min 6 characters)"
                    false
                }
                confirmPassword.isEmpty() -> {
                    etConfirmPassword.error = "Please confirm password"
                    false
                }
                password != confirmPassword -> {
                    etConfirmPassword.error = "Passwords don't match"
                    false
                }
                else -> true
            }
        }
    }

    private fun registerUser() {
        lifecycleScope.launch {
            try {
                with(binding) {
                    val user = User(
                        username = etUsername.text.toString(),
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString(), // Note: In production, hash this password
                        phone = etPhone.text?.toString()
                    )

                    val userId = userRepository.registerUser(user)

                    if (userId > 0) {
                        // Registration successful, navigate to interest selection
                        viewModel.register(etUsername.text.toString(), etEmail.text.toString(), etPassword.text.toString())
                        val interestsFragment = InterestSelectionFragment().apply {
                            arguments = Bundle().apply {
                                putInt("userId", userId.toInt())
                            }
                        }
                        (activity as? NavigationListener)?.navigateToFragment(interestsFragment)
                    } else {
                        Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("RegisterFragment", "Registration error", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}