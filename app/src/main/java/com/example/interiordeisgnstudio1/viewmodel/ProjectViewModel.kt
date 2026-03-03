package com.example.interiordeisgnstudio1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interiordeisgnstudio1.Model.ProjectModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Temporary workaround: Moved repository classes here to resolve import issues.
// Please consider renaming your packages to all lowercase.
interface ProjectRepo {
    suspend fun createProject(project: ProjectModel)
    suspend fun getAllProjects(userId: String): List<ProjectModel>
    suspend fun getProjectById(projectId: String): ProjectModel?
    suspend fun updateProject(project: ProjectModel)
    suspend fun deleteProject(projectId: String)
}

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

    override suspend fun deleteProject(projectId: String) {
        projectList.removeIf { it.id == projectId }
    }
}

class ProjectViewModel : ViewModel() {

    private val repository: ProjectRepo = ProjectRepoImpl()

    private val _projectList = MutableStateFlow<List<ProjectModel>>(emptyList())
    val projectList: StateFlow<List<ProjectModel>> = _projectList

    fun loadProjects(userId: String) {
        viewModelScope.launch {
            _projectList.value = repository.getAllProjects(userId)
        }
    }

    fun createProject(project: ProjectModel) {
        viewModelScope.launch {
            repository.createProject(project)
            loadProjects(project.userId)
        }
    }

    fun deleteProject(projectId: String, userId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            loadProjects(userId)
        }
    }
}
