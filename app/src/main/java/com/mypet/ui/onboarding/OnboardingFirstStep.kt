package com.mypet.ui.onboarding

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mypet.R
import com.mypet.databinding.FragmentOnboardingFirstStepBinding
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class OnboardingFirstStep : Fragment() {
    private var _binding: FragmentOnboardingFirstStepBinding? = null
    private val viewModel: OnboardingFirstStepViewModel by viewModels()
    private val binding get() = _binding!!
    private val googleSignInActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode==RESULT_OK)
            it.data?.let { intent -> viewModel.firebaseAuthWithGoogle(intent) }
        }

    private fun observeGoogleSignInSuccess() {
        viewModel.loginSuccessLiveData.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(R.id.action_onboardingFirstStep_to_onboardingSecondStep)
            }
        }
    }

    private fun observeGoogleSignInError() {
        viewModel.loginErrorLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Snackbar.make(
                    binding.root,
                    "An error occurred please try again later",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingFirstStepBinding.inflate(inflater, container, false)

        observeGoogleSignInError()
        observeGoogleSignInSuccess()
        (binding.createAccBtn.getChildAt(0) as TextView).text =
            getString(R.string.sign_in_with_google)
        startAnim()

        binding.signInLater.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFirstStep_to_onboardingSecondStep)
        }
        binding.createAccBtn.setOnClickListener {
            val gsc = viewModel.initGoogleSignInRequest(requireActivity())
            googleSignInActivityLauncher.launch(gsc.signInIntent)
        }

        return binding.root

    }

    private fun startAnim() {
        val animation = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left)
        animation.duration = 1000
        binding.root.startAnimation(animation)
    }
}