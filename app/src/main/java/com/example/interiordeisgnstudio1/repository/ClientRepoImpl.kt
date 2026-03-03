package com.example.interiordeisgnstudio1.repository

import android.net.Uri
import com.example.interiordeisgnstudio1.Model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ClientRepositoryImpl : ClientRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference

    private fun getUid(): String {
        return auth.currentUser?.uid
            ?: throw Exception("User not logged in")
    }

    override suspend fun getProject(): ProjectModel {
        return try {
            val uid = getUid()
            val snapshot = database.child("projects").child(uid).get().await()
            snapshot.getValue(ProjectModel::class.java) ?: ProjectModel()
        } catch (e: Exception) {
            ProjectModel()
        }
    }

    override suspend fun getDesigns(): List<Design> {
        return try {
            val uid = getUid()
            val snapshot = database.child("projects").child(uid).child("designs").get().await()
            snapshot.children.mapNotNull { it.getValue(Design::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTimeline(): List<ProjectTimeline> {
        return try {
            val uid = getUid()
            val snapshot = database.child("projects").child(uid).child("timeline").get().await()
            snapshot.children.mapNotNull { it.getValue(ProjectTimeline::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getUser(): UserModel {
        return try {
            val uid = getUid()
            val snapshot = database.child("users").child(uid).get().await()
            snapshot.getValue(UserModel::class.java) ?: UserModel()
        } catch (e: Exception) {
            UserModel()
        }
    }

    override suspend fun updateUser(user: UserModel): Boolean {
        return try {
            val uid = getUid()
            database.child("users").child(uid).setValue(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addProjectUpdate(update: UpdateModel): Boolean {
        return try {
            val uid = getUid()
            val projectRef = database.child("projects").child(uid).child("recentUpdates")
            
            val snapshot = projectRef.get().await()
            val currentUpdates = snapshot.children.mapNotNull { it.getValue(UpdateModel::class.java) }.toMutableList()
            
            currentUpdates.add(0, update)
            projectRef.setValue(currentUpdates).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateTimelineStatus(phaseTitle: String, newStatus: String): Boolean {
        return try {
            val uid = getUid()
            val timelineRef = database.child("projects").child(uid).child("timeline")
            
            val snapshot = timelineRef.get().await()
            val timeline = snapshot.children.mapNotNull { it.getValue(ProjectTimeline::class.java) }.toMutableList()
            
            val index = timeline.indexOfFirst { it.title == phaseTitle }
            if (index != -1) {
                timeline[index] = timeline[index].copy(status = newStatus)
                timelineRef.setValue(timeline).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun initializeDefaultTimeline(): Boolean {
        return try {
            val uid = getUid()
            val defaultTimeline = listOf(
                ProjectTimeline("Site Measurement", "Completed"),
                ProjectTimeline("Concept Design", "Completed"),
                ProjectTimeline("Material Selection", "In Progress"),
                ProjectTimeline("Execution Phase", "Pending")
            )
            database.child("projects").child(uid).child("timeline").setValue(defaultTimeline).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addProductToPhase(phaseTitle: String, product: ProductModel): Boolean {
        return try {
            val uid = getUid()
            val timelineRef = database.child("projects").child(uid).child("timeline")
            
            val snapshot = timelineRef.get().await()
            val timeline = snapshot.children.mapNotNull { it.getValue(ProjectTimeline::class.java) }.toMutableList()
            
            val index = timeline.indexOfFirst { it.title == phaseTitle }
            if (index != -1) {
                val currentProducts = timeline[index].products.toMutableList()
                currentProducts.add(product)
                timeline[index] = timeline[index].copy(products = currentProducts)
                timelineRef.setValue(timeline).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun uploadDesign(title: String, imageUri: Uri): Boolean {
        return try {
            val uid = getUid()
            val fileName = "design_${System.currentTimeMillis()}.jpg"
            val imageRef = storage.child("designs/$uid/$fileName")
            
            // Upload to Storage
            imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await().toString()
            
            // Save to Database
            val designRef = database.child("projects").child(uid).child("designs")
            val snapshot = designRef.get().await()
            val currentDesigns = snapshot.children.mapNotNull { it.getValue(Design::class.java) }.toMutableList()
            
            currentDesigns.add(Design(title, downloadUrl))
            designRef.setValue(currentDesigns).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getPaymentHistory(): List<PaymentModel> {
        return try {
            val uid = getUid()
            val snapshot = database.child("payments").child(uid).get().await()
            snapshot.children.mapNotNull { it.getValue(PaymentModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun makePayment(payment: PaymentModel): Boolean {
        return try {
            val uid = getUid()
            val paymentId = database.child("payments").child(uid).push().key ?: return false
            val finalPayment = payment.copy(id = paymentId)
            database.child("payments").child(uid).child(paymentId).setValue(finalPayment).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getNotifications(): List<NotificationModel> {
        return try {
            val uid = getUid()
            val snapshot = database.child("notifications").child(uid).get().await()
            snapshot.children.mapNotNull { it.getValue(NotificationModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Boolean {
        return try {
            val uid = getUid()
            database.child("notifications").child(uid).child(notificationId).child("isRead").setValue(true).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
