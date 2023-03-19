package com.mypet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [Breed::class,Pet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {


    abstract fun breedDao(): BreedDao
    abstract fun petDao(): PetDao


    companion object {
        private const val DB_NAME = "pets-db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(appContext: Context) = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(appContext, AppDatabase::class.java, DB_NAME)
                .createFromAsset("database/$DB_NAME.db")
                .build()
                .also { instance = it }
        }

    }
}