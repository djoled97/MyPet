package com.mypet.ui.onboarding

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mypet.R
import com.mypet.data.PetDto
import com.mypet.databinding.FragmentOnboardingSecondStepBinding
import com.mypet.util.SharedPreferencesHelper

import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class OnboardingSecondStep : Fragment() {
    private var _binding: FragmentOnboardingSecondStepBinding? = null
    private val viewModel: OnboardingSecondStepViewModel by viewModels()
    private val binding get() = _binding!!
    private lateinit var snackbar: Snackbar
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
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
            findNavController().navigate(R.id.action_onboardingSecondStep_to_onboardingFirstStep)
        }
        binding.btnNext.setOnClickListener {
            val name = binding.petName.text.toString()
            val type = binding.petType.text.toString()
            val color = binding.petColor.text.toString()
            val breed = binding.petBreed.text.toString()
            val gender = binding.petGender.text.toString()
            val birthday = binding.petBirthday.text.toString()
            val microchip = binding.petMicrochip.text.toString()
            viewModel.createPet(PetDto(name, breed, type, gender, color, birthday, microchip))
            sharedPreferencesHelper.updateOnboardingValue()
            requireActivity().finish()

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
}

