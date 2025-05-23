package com.example.noteminds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteminds.R
import androidx.lifecycle.lifecycleScope
import com.example.noteminds.NavigationListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
Craeted by Abdul Mueez 04/16/2025
 */
class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Delay for 2 seconds before navigating to LoginFragment
        lifecycleScope.launch {
            delay(2000)  // 2000 ms = 2 seconds
            (activity as? NavigationListener)?.navigateToFragment(LoginFragment(), false)
        }
    }
}
