package com.mypet.ui.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypet.data.Event
import com.mypet.databinding.EventItemRowBinding
import java.text.SimpleDateFormat
import java.util.Locale

class EventsAdapter(val events: List<Event>) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    class ViewHolder(val binding: EventItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            EventItemRowBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val element = events[position]

        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(element.date)
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("E", Locale.getDefault())

        with(viewHolder) {
            binding.time.text = element.time
            binding.title.text = element.name
            binding.date.text = date?.let { dateFormat.format(it) }
            binding.day.text = date?.let { dayFormat.format(it) }
        }
    }

    override fun getItemCount() = events.size

}