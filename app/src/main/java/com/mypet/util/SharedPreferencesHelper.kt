package com.mypet.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(val context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).apply {
            edit().putBoolean(ONBOARDING, true).apply()
        }
    val onboarding = preferences.getBoolean(ONBOARDING, true)

    fun updateOnboardingValue() = preferences.edit().putBoolean(ONBOARDING, onboarding).apply()


    companion object {
        private const val PREF_NAME = "app_preferences"
        private const val ONBOARDING = "onboarding"

    }
}