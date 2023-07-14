package com.mypet

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.mypet.databinding.ActivityMainBinding
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
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
        navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        if ( firebaseAuth.currentUser==null && sharedPreferencesHelper.onboarding) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }else{
            navController.addOnDestinationChangedListener { _, _, _ ->
                handleToolbarTitle()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        handleToolbarTitle()
        return super.onOptionsItemSelected(item)

    }

    private fun handleToolbarTitle() {
        if (navController.currentDestination?.id == R.id.navigation_home) {
            binding.toolbar.title = getString(R.string.home_title)

        } else {
            binding.toolbar.title = "Events"

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}