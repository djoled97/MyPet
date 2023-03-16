package com.mypet.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mypet.R
import com.mypet.databinding.FragmentOnboardingFirstStepBinding
import com.mypet.databinding.FragmentOnboardingSecondStepBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.regex.Pattern


@AndroidEntryPoint
class OnboardingSecondStep : Fragment() {
    private var _binding: FragmentOnboardingSecondStepBinding? = null
    private val binding get() = _binding!!
    var isRegistrationForm = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingSecondStepBinding.inflate(inflater, container, false)
        binding.password.doOnTextChanged { _, _, _, _ -> clearErrors() }
        binding.confirmPassword.doOnTextChanged { _, _, _, _ -> clearErrors() }

        binding.btnBefore.setOnClickListener {
            if (isRegistrationForm) {
                findNavController().navigate(R.id.action_onboardingSecondStep_to_onboardingFirstStep)
            } else {
                swapForms()
            }
        }
        binding.btnNext.setOnClickListener {

            if (isRegistrationForm) {
                registerFormValidation()
                if (binding.passwordLayout.error == null && binding.confirmPasswordLayout.error == null) {
                    swapForms()

                }


            } else {

            }
        }



        return binding.root

    }

    private fun registerFormValidation() {
        if (!isPassValid()) binding.passwordLayout.error =
            getString(R.string.registration_form_password_error_text)
        if (binding.password.text.toString() != binding.confirmPassword.text.toString()) binding.confirmPasswordLayout.error =
            getString(R.string.registration_form_confirm_password_error_text)
    }

    private fun swapForms() {
        isRegistrationForm = !isRegistrationForm
        if (isRegistrationForm) {
            binding.petEntryForm.visibility = View.GONE
            binding.registrationForm.visibility = View.VISIBLE
        } else {
            binding.petEntryForm.visibility = View.VISIBLE
            binding.registrationForm.visibility = View.GONE
        }


    }

    private fun clearErrors() {
        binding.passwordLayout.error = null
        binding.confirmPasswordLayout.error = null
    }

    private fun isPassValid(): Boolean {
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
        return Pattern.compile(regex).matcher(binding.password.text.toString()).matches()

    }

}