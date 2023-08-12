package com.mypet.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {

    @Insert
    fun createEvent(event: Event)

    @Query("SELECT * from event")
    fun getAllEvents(): List<Event>
}