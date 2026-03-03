package com.example.interiordeisgnstudio1.Model

data class PaymentModel(
    val id: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val status: String = "", // e.g., "Success", "Pending", "Failed"
    val description: String = ""
)
