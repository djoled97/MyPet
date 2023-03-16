package com.mypet.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _isRegisteredLiveData = MutableLiveData(false)
    val isRegisteredLiveData: LiveData<Boolean>
        get() = _isRegisteredLiveData

    suspend fun createUser(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            _isRegisteredLiveData.postValue(true)
        } catch (exception: Exception) {
            _isRegisteredLiveData.postValue(false)
        }

    }
}