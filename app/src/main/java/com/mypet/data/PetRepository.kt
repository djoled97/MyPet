package com.mypet.data

import javax.inject.Inject
import javax.inject.Singleton

class PetRepository @Inject constructor(private val petDao: PetDao) {

    fun createPet(pet: Pet) = petDao.createPet(pet)

}