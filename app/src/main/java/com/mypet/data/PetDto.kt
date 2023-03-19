package com.mypet.data

data class PetDto(
    val name: String,
    val breed: String,
    val type: String,
    val gender: String,
    val color: String,
    val birthday: String,
    val microChip: String
) {

    fun toPet(breedId: Long) = Pet(0, name,breedId, gender, color, birthday, microChip)

}
