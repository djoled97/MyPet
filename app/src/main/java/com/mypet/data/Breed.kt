package com.mypet.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Breed(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: String,
    val breed: String?

) {

}
