package com.mypet

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.mypet.databinding.ActivityMainBinding
import com.mypet.databinding.FragmentHomeBinding
import com.mypet.ui.onboarding.OnboardingActivity
import com.mypet.util.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            _binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            val navView: BottomNavigationView = binding.navView
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
            val navController = navHostFragment.navController
            navView.setupWithNavController(navController)
        if (firebaseAuth.currentUser == null && sharedPreferencesHelper.onboarding) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}