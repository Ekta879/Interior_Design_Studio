package com.example.interiordeisgnstudio1.repository

import com.example.interiordeisgnstudio1.Model.ProjectModel
import com.example.interiordeisgnstudio1.Model.ProjectTimeline


interface ProjectRepo {

    suspend fun createProject(project: ProjectModel)

    suspend fun getAllProjects(userId: String): List<ProjectModel>

    suspend fun getProjectById(projectId: String): ProjectModel?

    suspend fun updateProject(project: ProjectModel)

    suspend fun getTimeline(): List<ProjectTimeline>   // ✅ MUST BE HERE

    suspend fun deleteProject(projectId: String)
}