package com.mypet.ui.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mypet.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class OnboardingFirstStepViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _loginSuccessLiveData = MutableLiveData<Unit>(null)
    val loginSuccessLiveData: LiveData<Unit>
        get() = _loginSuccessLiveData

    private val _loginErrorLiveData = MutableLiveData<Unit>(null)
    val loginErrorLiveData: LiveData<Unit>
        get() = _loginErrorLiveData

    fun initGoogleSignInRequest(activityContext: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(
                activityContext.getString(R.string.default_web_client_id)
            )
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activityContext, gso)
    }

    fun firebaseAuthWithGoogle(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val acc = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
            val credential = GoogleAuthProvider.getCredential(acc.idToken, null)
            try {
                auth.signInWithCredential(credential).await()
                _loginSuccessLiveData.postValue(Unit)
            } catch (exception: Exception) {
                _loginErrorLiveData.postValue(Unit)
            }
        }
    }

}