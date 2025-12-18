package com.example.interiordeisgnstudio1

data class UserModel(
    val id : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
){
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "id" to id,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
        )
    }
}