package com.example.noteminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.noteminds.ui.SplashScreenFragment
import com.example.noteminds.data.repository.UserRepository
import com.example.noteminds.data.room.AppDatabase

import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

/**
Created by Abdul Mueez 04/16/2025
 */
class MainActivity : AppCompatActivity(), NavigationListener {
    private lateinit var appDatabase: AppDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var currentUser: FirebaseUser? = null
    public var currentUser1: com.example.noteminds.ui.model.FirebaseUser? = null
    private lateinit var userRepository: UserRepository
    public var flag=true;
    public var SummaryText:String="";

    companion object {
        public const val LOAD_PAYMENT_DATA_REQUEST_CODE = 991
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_NoteMinds) // I have Set the main theme manually to remove splash
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        // Check if user is already logged in
        currentUser = auth.currentUser
        // Load the login fragment by default
        navigateToFragment(SplashScreenFragment(), false)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val paymentData = PaymentData.getFromIntent(data!!)
                        val info = paymentData?.toJson()
                        Log.d("GooglePay", "Payment info: $info")
                        // Send this info to your server
                    }
                    RESULT_CANCELED -> {
                        // The user cancelled
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        Log.e("GooglePay", "Error: ${status?.statusMessage}")

                    }
                }
            }
        }
    }
    override fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }
    fun setCurrentUser(user: com.example.noteminds.ui.model.FirebaseUser) {
        this.currentUser1 = user
        System.out.println("Current User ----")
        System.out.println(currentUser)
    }

    fun getCurrentUser(): FirebaseUser? {
        return currentUser ?: auth.currentUser
    }

    fun getCurrentUserId(): String {
//        return currentUser?.uid ?: auth.currentUser?.uid ?: ""
        return this.currentUser1?.id.toString()
}
    fun getDatabase(): FirebaseDatabase {
        return database
    }
}

interface NavigationListener {
    fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = true)
}