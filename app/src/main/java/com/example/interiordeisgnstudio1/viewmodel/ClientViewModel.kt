package com.example.interiordeisgnstudio1.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interiordeisgnstudio1.Model.*
import com.example.interiordeisgnstudio1.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClientViewModel(
    private val repository: ClientRepository = ClientRepositoryImpl()
) : ViewModel() {

    private val _project = MutableStateFlow<ProjectModel?>(null)
    val project: StateFlow<ProjectModel?> = _project

    private val _designs = MutableStateFlow<List<Design>>(emptyList())
    val designs: StateFlow<List<Design>> = _designs

    private val _timeline = MutableStateFlow<List<ProjectTimeline>>(emptyList())
    val timeline: StateFlow<List<ProjectTimeline>> = _timeline

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _payments = MutableStateFlow<List<PaymentModel>>(emptyList())
    val payments: StateFlow<List<PaymentModel>> = _payments

    private val _notifications = MutableStateFlow<List<NotificationModel>>(emptyList())
    val notifications: StateFlow<List<NotificationModel>> = _notifications

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    init {
        loadAllData()
    }

    fun loadAllData() {
        viewModelScope.launch {
            _project.value = repository.getProject()
            _designs.value = repository.getDesigns()
            _timeline.value = repository.getTimeline()
            _user.value = repository.getUser()
            _payments.value = repository.getPaymentHistory()
            _notifications.value = repository.getNotifications()
        }
    }

    fun updateUser(user: UserModel, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.updateUser(user)
            if (success) {
                _user.value = user
            }
            onResult(success)
        }
    }

    fun updateTimelineStatus(phaseTitle: String, newStatus: String) {
        viewModelScope.launch {
            val success = repository.updateTimelineStatus(phaseTitle, newStatus)
            if (success) {
                _timeline.value = repository.getTimeline() // Refresh list
            }
        }
    }

    fun initializeDefaultTimeline() {
        viewModelScope.launch {
            val success = repository.initializeDefaultTimeline()
            if (success) {
                _timeline.value = repository.getTimeline() // Refresh list
            }
        }
    }

    fun addProductToPhase(phaseTitle: String, product: ProductModel) {
        viewModelScope.launch {
            val success = repository.addProductToPhase(phaseTitle, product)
            if (success) {
                _timeline.value = repository.getTimeline() // Refresh list
            }
        }
    }

    fun uploadDesign(title: String, imageUri: Uri, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isUploading.value = true
            val success = repository.uploadDesign(title, imageUri)
            if (success) {
                _designs.value = repository.getDesigns() // Refresh list
            }
            _isUploading.value = false
            onComplete(success)
        }
    }

    fun processPayment(amount: Double, description: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val payment = PaymentModel(
                amount = amount,
                description = description,
                date = "March 3, 2024", 
                status = "Success"
            )
            val success = repository.makePayment(payment)
            if (success) {
                _payments.value = repository.getPaymentHistory()
            }
            onResult(success)
        }
    }

    fun markNotificationAsRead(notificationId: String) {
        viewModelScope.launch {
            val success = repository.markNotificationAsRead(notificationId)
            if (success) {
                _notifications.value = repository.getNotifications()
            }
        }
    }
}
