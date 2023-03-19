package com.mypet.data

import androidx.room.*


@Entity(
    foreignKeys = [ForeignKey(
        entity = Breed::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("breedId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val breedId: Long,
    val gender: String,
    val color: String,
    val birthday: String,
    val microChip: String

)