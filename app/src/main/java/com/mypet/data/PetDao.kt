package com.mypet.data

import androidx.room.Dao
import androidx.room.Insert


@Dao
interface PetDao {
    @Insert
    fun createPet(pet: Pet)
}