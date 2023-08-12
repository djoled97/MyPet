package com.mypet.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface PetDao {
    @Insert
    fun createPet(pet: Pet)

    @Query("SELECT * from Pet LIMIT 1")
    fun getPet():Pet
}