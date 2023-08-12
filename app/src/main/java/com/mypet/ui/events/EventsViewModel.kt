package com.mypet.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mypet.data.Event
import com.mypet.data.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    private val _eventListLiveData = MutableLiveData<List<Event>>()
    val eventListLiveData: LiveData<List<Event>> = _eventListLiveData

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    fun createEvent(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.createEvent(event)
    }

    fun getAllEvents() = viewModelScope.launch(Dispatchers.IO) {
        val listOfEvents = eventRepository.getAllEvents()
        _eventListLiveData.postValue(listOfEvents)
    }
}