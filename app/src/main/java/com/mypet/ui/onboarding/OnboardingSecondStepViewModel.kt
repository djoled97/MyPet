package com.mypet.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mypet.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class OnboardingSecondStepViewModel @Inject constructor(

    private val breedRepository: BreedRepository,
    private val petRepository: PetRepository

) : ViewModel() {




    private val _breedLiveData = MutableLiveData<List<String>>()
    val breedLiveData: LiveData<List<String>>
        get() = _breedLiveData

    fun getBreedsByType(type: String) = viewModelScope.launch(Dispatchers.IO) {
        val breeds = breedRepository.getBreedsByType(type)
        _breedLiveData.postValue(breeds)
    }

    fun createPet(petDto: PetDto) = viewModelScope.launch(Dispatchers.IO) {
        val breedId = breedRepository.getIdByBreed(petDto.breed)
        if (breedId != null) {
            petRepository.createPet(petDto.toPet(breedId))
        } else {
            val id = breedRepository.addType(Breed(0, petDto.type, null))
            val pet = petDto.toPet(id)
            petRepository.createPet(pet)
        }


    }
}