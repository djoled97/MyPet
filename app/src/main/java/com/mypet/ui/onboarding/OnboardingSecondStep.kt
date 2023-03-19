package com.mypet.ui.onboarding

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mypet.R
import com.mypet.data.Pet
import com.mypet.data.PetDto
import com.mypet.databinding.FragmentOnboardingSecondStepBinding

import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


@AndroidEntryPoint
class OnboardingSecondStep : Fragment() {
    private var _binding: FragmentOnboardingSecondStepBinding? = null
    private val viewModel: OnboardingViewModel by viewModels()
    private val binding get() = _binding!!
    var isRegistrationForm = true
    private lateinit var snackbar: Snackbar

    lateinit var dogSuggestionAdapter: ArrayAdapter<String>
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
        binding.petType.doOnTextChanged { text, _, _, _ ->
            val textStr = text.toString().lowercase()
            if (textStr == "cat" || textStr == "dog") {
                binding.petBreedLayout.isEnabled = true
                binding.petBreedLayout.alpha = 1f

                viewModel.getBreedsByType(textStr)
            } else if (this::dogSuggestionAdapter.isInitialized) {
                dogSuggestionAdapter.clear()
                dogSuggestionAdapter.notifyDataSetChanged()
                binding.petBreedLayout.isEnabled = false
                binding.petBreedLayout.alpha = 0.2f

            }

        }

        binding.btnBefore.setOnClickListener {
            if (isRegistrationForm) {
                findNavController().navigate(R.id.action_onboardingSecondStep_to_onboardingFirstStep)
            } else {
                swapForms()
            }
        }
        binding.btnNext.setOnClickListener {
            val isValid = registerFormValidation()
            if (isRegistrationForm && isValid) {
                swapForms()
            } else if (isValid) {
                viewModel.createUser(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
            }


        }


        viewModel.isRegisteredLiveData.observe(viewLifecycleOwner) {
            if (it) {
                val name = binding.petName.text.toString()
                val type = binding.petType.text.toString()
                val color = binding.petColor.text.toString()
                val breed = binding.petBreed.text.toString()
                val gender = binding.petGender.text.toString()
                val birthday = binding.petBirthday.text.toString()
                val microchip = binding.petMicrochip.text.toString()
                viewModel.createPet(PetDto(name, breed, type, gender, color, birthday, microchip))
                requireActivity().finish()
            }

        }
        viewModel.breedLiveData.observe(viewLifecycleOwner) {
            dogSuggestionAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item,
                it,
            )
            (binding.petBreed as AutoCompleteTextView).setAdapter(
                dogSuggestionAdapter
            )
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = Snackbar.make(
            binding.root,
            "All fields are required",
            Snackbar.LENGTH_SHORT
        )
    }

    private fun isOnboardingFormValid(): Boolean {
        with(binding) {
            val formData = if (isRegistrationForm) {
                listOf(
                    binding.password.text.toString(),
                    binding.confirmPassword.text.toString(),
                    binding.email.text.toString()
                )
            } else {
                val petBday =
                    if (petBirthday.text.toString() == "DD/MM/YYYY") "" else petBirthday.text.toString()
                listOf(
                    petName.text.toString(),
                    petBday,
                    petBreed.text.toString(),
                    petType.text.toString(),
                    petGender.text.toString(),
                    petColor.text.toString(),
                    petMicrochip.text.toString()
                )
            }

            return formData.any { it != "" }
        }
    }

    private fun registerFormValidation(): Boolean {
        when {
            !isOnboardingFormValid() -> {
                snackbar.show()
                return false
            }
            !isPassValid() -> {
                binding.passwordLayout.error =
                    getString(R.string.registration_form_password_error_text)
                return false

            }
            binding.password.text.toString() != binding.confirmPassword.text.toString() -> {
                binding.confirmPasswordLayout.error =
                    getString(R.string.registration_form_confirm_password_error_text)
                return false
            }
            else -> {
                return true
            }
        }
//        if (!isPassValid())
//        if (binding.password.text.toString() != binding.confirmPassword.text.toString()) binding.confirmPasswordLayout.error =
//            getString(R.string.registration_form_confirm_password_error_text)
    }

    private fun swapForms() {
        isRegistrationForm = !isRegistrationForm
        val animation =
            AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
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