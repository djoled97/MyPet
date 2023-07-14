package com.mypet.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesHelper @Inject constructor(@ApplicationContext context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    val onboarding = preferences.getBoolean(ONBOARDING, true)

    private  val editor=preferences.edit()
    fun updateOnboardingValue() = editor.putBoolean(ONBOARDING, false).commit()


    companion object {
        private const val PREF_NAME = "app_preferences"
        private const val ONBOARDING = "onboarding"

    }
}