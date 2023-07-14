package com.mypet.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BreedDao {

    @Query("SELECT breed FROM Breed where type LIKE :type")
    fun getBreedsByType(type:String): List<String>

    @Query("SELECT id FROM Breed where breed LIKE :breed")
    fun getIdByBreed(breed:String):Long?

    @Insert
    fun addType(breed:Breed):Long

    @Query("SELECT * FROM Breed where id LIKE :id")
    fun getBreedById(id: Long): Breed

}