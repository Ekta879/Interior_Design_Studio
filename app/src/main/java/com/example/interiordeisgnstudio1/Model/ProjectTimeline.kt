package com.example.interiordeisgnstudio1.Model

data class ProjectTimeline(
    val title: String = "",
    val status: String = "Pending",
    val products: List<ProductModel> = emptyList()
)