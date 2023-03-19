package com.mypet.data

import javax.inject.Inject
import javax.inject.Singleton

class BreedRepository @Inject constructor(private val breedDao: BreedDao) {

    fun getBreedsByType(type: String) = breedDao.getBreedsByType(type)

    fun getIdByBreed(breed: String) = breedDao.getIdByBreed(breed)

    fun addType(breed: Breed) = breedDao.addType(breed)
}