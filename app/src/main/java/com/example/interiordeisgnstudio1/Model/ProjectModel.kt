package com.example.interiordeisgnstudio1.Model

data class ProjectModel(
    val id: String = "",
    val userId: String = "",
    val projectName: String = "",
    val progress: Double = 0.0,
    val totalBudget: Double = 0.0,
    val paidAmount: Double = 0.0,
    val remainingAmount: Double = 0.0,
    val timeline: List<ProjectTimeline> = emptyList(),
    val recentUpdates: List<UpdateModel> = emptyList()
)

data class UpdateModel(
    val text: String = "",
    val time: String = ""
)
