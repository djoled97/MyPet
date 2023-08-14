package com.mypet.ui.events

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mypet.NotificationWorkerManager
import com.mypet.NotificationWorkerManager.Companion.NOTIFICATION_NOTES
import com.mypet.NotificationWorkerManager.Companion.NOTIFICATION_TITLE
import com.mypet.data.Event
import com.mypet.data.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    private val _eventListLiveData = MutableLiveData<List<Event>>()
    val eventListLiveData: LiveData<List<Event>> = _eventListLiveData

    private val _eventAddedLiveData = MutableLiveData<Boolean>()
    val eventAddedLiveData: LiveData<Boolean> = _eventAddedLiveData

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private fun createEvent(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.createEvent(event)
    }

    fun createEventAndNotification(event: Event, ctx: Context) {
        viewModelScope.launch {
            createEvent(event)
            val oneTimeRequest = OneTimeWorkRequestBuilder<NotificationWorkerManager>()
                .setInitialDelay(60000, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        NOTIFICATION_TITLE to event.name,
                        NOTIFICATION_NOTES to event.notes
                    )
                ).build()
            WorkManager.getInstance(ctx).enqueue(oneTimeRequest)
            _eventAddedLiveData.postValue(true)
        }
    }

    fun getAllEvents() = viewModelScope.launch(Dispatchers.IO) {
        val listOfEvents = eventRepository.getAllEvents()
        _eventListLiveData.postValue(listOfEvents)
    }
}