package com.mypet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mypet.data.BreedRepository
import com.mypet.data.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val petRepository: PetRepository, private val breedRepository:BreedRepository) : ViewModel() {

    private val _petPairValueList = MutableLiveData<List<Pair<String,String>>>()
    val petPairValueList: LiveData<List<Pair<String,String>>> = _petPairValueList

    fun getPet(){
        viewModelScope.launch(Dispatchers.IO) {
            val petPairValueList=  petRepository.getPet().mapPetToPairValueList().toMutableList()
            breedRepository.getBreedById(petPairValueList[2].second.toLong()).breed?.let {
                petPairValueList[2]=Pair("breed",it)
            }
            _petPairValueList.postValue(petPairValueList)
        }
    }
}