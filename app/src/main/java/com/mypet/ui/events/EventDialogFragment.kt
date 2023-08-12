package com.mypet.ui.events

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.mypet.R
import com.mypet.data.Event
import com.mypet.databinding.DialogFragmentEventBinding
import com.mypet.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EventDialogFragment : DialogFragment() {


    private var _binding: DialogFragmentEventBinding? = null
    private val viewModel: EventsViewModel by viewModels()
    private val binding get() = _binding!!

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
                val minsWithTrailingZeros= if (picker.minute<=9) "0${picker.minute}" else picker.minute
                val hrsWithTrailingZeros=if (picker.hour<=9) "0${picker.hour}" else picker.hour
                val hrsStr = "${hrsWithTrailingZeros}:${minsWithTrailingZeros}"
                (binding.eventTime as TextView).text = hrsStr
            }
        }
        binding.saveBtn.setOnClickListener {

            viewModel.createEvent(
                Event(
                    0,
                    binding.eventName.text.toString(),
                    binding.eventDate.text.toString(),
                    binding.eventTime.text.toString(),
                    binding.eventNote.text.toString()
                )
            )
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}