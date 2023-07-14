package com.mypet.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mypet.data.AppDatabase
import com.mypet.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getInstance(appContext)

    @Provides
    @Singleton
    fun provideBreedDao(appDatabase: AppDatabase) = appDatabase.breedDao()

    @Provides
    @Singleton
    fun providePetDao(appDatabase: AppDatabase) = appDatabase.petDao()

}