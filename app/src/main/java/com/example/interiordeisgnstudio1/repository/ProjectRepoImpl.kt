package com.example.interiordeisgnstudio1.repository

import com.example.interiordeisgnstudio1.Model.ProjectModel
import com.example.interiordeisgnstudio1.Model.ProjectTimeline

class ProjectRepoImpl : ProjectRepo {

    private val projectList = mutableListOf<ProjectModel>()

    override suspend fun createProject(project: ProjectModel) {
        projectList.add(project)
    }

    override suspend fun getAllProjects(userId: String): List<ProjectModel> {
        return projectList.filter { it.userId == userId }
    }

    override suspend fun getProjectById(projectId: String): ProjectModel? {
        return projectList.find { it.id == projectId }
    }

    override suspend fun updateProject(project: ProjectModel) {
        val index = projectList.indexOfFirst { it.id == project.id }
        if (index != -1) {
            projectList[index] = project
        }
    }

    override suspend fun getTimeline(): List<ProjectTimeline> {
        return listOf(
            ProjectTimeline("Site Measurement", "Completed"),
            ProjectTimeline("Concept Design", "Completed"),
            ProjectTimeline("Material Selection", "In Progress"),
            ProjectTimeline("Execution Phase", "Pending")
        )
    }

    override suspend fun deleteProject(projectId: String) {
        projectList.removeIf { it.id == projectId }
    }
}