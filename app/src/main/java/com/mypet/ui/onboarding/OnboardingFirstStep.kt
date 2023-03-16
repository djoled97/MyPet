package com.mypet.ui.onboarding

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mypet.R
import com.mypet.databinding.FragmentOnboardingFirstStepBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnboardingFirstStep : Fragment() {
    private var _binding: FragmentOnboardingFirstStepBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingFirstStepBinding.inflate(inflater, container, false)
        startAnim()

        binding.createAccBtn.setOnClickListener{
            findNavController().navigate(R.id.action_onboardingFirstStep_to_onboardingSecondStep)
        }
        return binding.root

    }

    private fun startAnim() {
        val animation=AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left)
        animation.duration = 1000
        binding.root.startAnimation(animation)
    }
}