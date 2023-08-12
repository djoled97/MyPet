package com.mypet.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.mypet.MainActivity
import com.mypet.R
import com.mypet.data.BreedDao
import com.mypet.databinding.ActivityOnboardingBinding
import com.mypet.util.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!sharedPreferencesHelper.onboarding) {
            startActivity(Intent(this, MainActivity::class.java))
            }else {
            _binding = ActivityOnboardingBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}