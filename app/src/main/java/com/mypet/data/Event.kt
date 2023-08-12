package com.mypet.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name:String,
    val date:String,
    val time:String,
    val notes:String

)