package com.mypet.ui.onboarding

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mypet.R
import com.mypet.databinding.FragmentOnboardingSecondStepBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
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



        (binding.petGender as AutoCompleteTextView).setAdapter(
            ArrayAdapter(
                requireContext(), R.layout.list_item,
                listOf(
                    getString(R.string.spinner_data_female),
                    getString(R.string.spinner_data_male)
                )
            )
        )
        (binding.petMicrochip as AutoCompleteTextView).setAdapter(
            ArrayAdapter(
                requireContext(), R.layout.list_item,
                listOf(
                    getString(R.string.yes),
                    getString(R.string.no)
                )
            )
        )


        binding.petBirthdayLayout.setEndIconOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            DatePickerDialog(
                requireContext(),
                { _, y, monthOfYear, dayOfMonth ->
                    calendar.set(y, monthOfYear, dayOfMonth)
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    val dateAsString = sdf.format(calendar.time)
                    binding.petBirthday.setText(dateAsString)
                }, year, month, day
            ).let {
                it.datePicker.maxDate = Date().time
                it.show()
            }

        }

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
        val animation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        if (isRegistrationForm) {
            binding.petEntryForm.visibility = View.GONE
            binding.registrationForm.visibility = View.VISIBLE
            binding.registrationForm.startAnimation(animation)
        } else {
            binding.petEntryForm.visibility = View.VISIBLE
            binding.registrationForm.visibility = View.GONE
            binding.petEntryForm.startAnimation(animation)
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