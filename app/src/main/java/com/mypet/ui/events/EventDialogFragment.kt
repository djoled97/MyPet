package com.mypet.ui.events

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.mypet.R
import com.mypet.data.Event
import com.mypet.databinding.DialogFragmentEventBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EventDialogFragment : DialogFragment() {


    private var _binding: DialogFragmentEventBinding? = null
    private val viewModel: EventsViewModel by viewModels()


    private val binding get() = _binding!!
    private val pushNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                viewModel.createEventAndNotification(
                    Event(
                        0,
                        binding.eventName.text.toString(),
                        binding.eventDate.text.toString(),
                        binding.eventTime.text.toString(),
                        binding.eventNote.text.toString()
                    ), requireContext()
                )
            } else if (!granted && shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                showSettingDialog()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentEventBinding.inflate(inflater, container, false)
        viewModel.eventAddedLiveData.observe(viewLifecycleOwner) { isAdded ->
            if (isAdded) findNavController().navigate(R.id.action_eventDialogFragment_to_navigation_events)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_eventDialogFragment_to_navigation_events)
        }
        binding.eventDate.setOnClickListener {
            val calConstraints =
                CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build()
            val picker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
                .setCalendarConstraints(calConstraints)
                .build()

            picker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it
                val formattedDate =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                (binding.eventDate as TextView).text = formattedDate
            }
            picker.show(childFragmentManager, null)
        }
        binding.eventTime.setOnClickListener {
            val picker = MaterialTimePicker.Builder().setTitleText("Select time")
                .setInputMode(INPUT_MODE_CLOCK).setTimeFormat(CLOCK_24H)
                .build()

            picker.show(childFragmentManager, "")
            picker.addOnPositiveButtonClickListener {
                val minsWithTrailingZeros =
                    if (picker.minute <= 9) "0${picker.minute}" else picker.minute
                val hrsWithTrailingZeros = if (picker.hour <= 9) "0${picker.hour}" else picker.hour
                val hrsStr = "${hrsWithTrailingZeros}:${minsWithTrailingZeros}"
                (binding.eventTime as TextView).text = hrsStr
            }
        }
        binding.saveBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 33) {
                pushNotificationPermissionLauncher.launch(POST_NOTIFICATIONS)
            } else {
                viewModel.createEventAndNotification(
                    Event(
                        0,
                        binding.eventName.text.toString(),
                        binding.eventDate.text.toString(),
                        binding.eventTime.text.toString(),
                        binding.eventNote.text.toString()
                    ), requireContext()
                )
            }
        }
        return binding.root
    }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            requireContext(),
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}