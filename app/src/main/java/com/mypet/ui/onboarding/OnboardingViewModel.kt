package com.mypet.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _isRegisteredLiveData = MutableLiveData(false)
    val isRegisteredLiveData: LiveData<Boolean>
        get() = _isRegisteredLiveData

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _isRegisteredLiveData.postValue(true)
            } catch (exception: Exception) {
                _isRegisteredLiveData.postValue(false)
            }

        }
    }
}