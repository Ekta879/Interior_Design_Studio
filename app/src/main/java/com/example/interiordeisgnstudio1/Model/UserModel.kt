package com.example.interiordeisgnstudio1.Model

data class UserModel(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val dob: String = "",
    val email: String = "",
    val role: String = "",
    val phone : String=" ",
// designer / client
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "firstName" to firstName,
            "lastName" to lastName,
            "gender" to gender,
            "dob" to dob,
            "phone" to phone,
            "email" to email,
            "role" to role
        )
    }
}
