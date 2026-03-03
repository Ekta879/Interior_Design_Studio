package com.example.interiordeisgnstudio1.repository

import android.net.Uri
import com.example.interiordeisgnstudio1.Model.*

interface ClientRepository {
    suspend fun getProject(): ProjectModel
    suspend fun getDesigns(): List<Design>
    suspend fun getTimeline(): List<ProjectTimeline>
    suspend fun getUser(): UserModel
    suspend fun updateUser(user: UserModel): Boolean
    suspend fun addProjectUpdate(update: UpdateModel): Boolean
    suspend fun updateTimelineStatus(phaseTitle: String, newStatus: String): Boolean
    suspend fun initializeDefaultTimeline(): Boolean
    suspend fun addProductToPhase(phaseTitle: String, product: ProductModel): Boolean
    suspend fun uploadDesign(title: String, imageUri: Uri): Boolean
    suspend fun getPaymentHistory(): List<PaymentModel>
    suspend fun makePayment(payment: PaymentModel): Boolean
    suspend fun getNotifications(): List<NotificationModel>
    suspend fun markNotificationAsRead(notificationId: String): Boolean
}