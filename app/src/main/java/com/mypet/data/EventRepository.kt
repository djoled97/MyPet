package com.mypet.data

import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao) {

    fun createEvent(event: Event) = eventDao.createEvent(event)
    fun getAllEvents() = eventDao.getAllEvents()
}