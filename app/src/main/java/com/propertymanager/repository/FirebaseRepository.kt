package com.propertymanager.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.propertymanager.models.*
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Auth functions
    suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // User functions
    suspend fun createUser(user: User): Result<Unit> {
        return try {
            db.collection("users").document(user.id).set(user.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<User?> {
        return try {
            val doc = db.collection("users").document(userId).get().await()
            val user = doc.data?.let { User.fromMap(it) }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            db.collection("users").document(user.id).set(user.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Property functions
    suspend fun createProperty(property: Property): Result<String> {
        return try {
            val docRef = db.collection("properties").document()
            val propertyWithId = property.copy(id = docRef.id)
            docRef.set(propertyWithId.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProperties(ownerId: String): Result<List<Property>> {
        return try {
            val snapshot = db.collection("properties")
                .whereEqualTo("ownerId", ownerId)
                .get()
                .await()
            val properties = snapshot.documents.mapNotNull { it.data?.let { data -> Property.fromMap(data) } }
            Result.success(properties)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProperty(propertyId: String): Result<Property?> {
        return try {
            val doc = db.collection("properties").document(propertyId).get().await()
            val property = doc.data?.let { Property.fromMap(it) }
            Result.success(property)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Tenant functions
    suspend fun createTenant(tenant: Tenant): Result<String> {
        return try {
            val docRef = db.collection("tenants").document()
            val tenantWithId = tenant.copy(
                id = docRef.id,
                inviteCode = generateInviteCode()
            )
            docRef.set(tenantWithId.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTenantsByProperty(propertyId: String): Result<List<Tenant>> {
        return try {
            val snapshot = db.collection("tenants")
                .whereEqualTo("propertyId", propertyId)
                .get()
                .await()
            val tenants = snapshot.documents.mapNotNull { it.data?.let { data -> Tenant.fromMap(data) } }
            Result.success(tenants)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTenantByInviteCode(code: String): Result<Tenant?> {
        return try {
            val snapshot = db.collection("tenants")
                .whereEqualTo("inviteCode", code)
                .limit(1)
                .get()
                .await()
            val tenant = snapshot.documents.firstOrNull()?.data?.let { Tenant.fromMap(it) }
            Result.success(tenant)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTenant(tenant: Tenant): Result<Unit> {
        return try {
            db.collection("tenants").document(tenant.id).set(tenant.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActiveTenantForUser(userId: String): Result<Tenant?> {
        return try {
            val snapshot = db.collection("tenants")
                .whereEqualTo("userId", userId)
                .whereEqualTo("isActive", true)
                .limit(1)
                .get()
                .await()
            val tenant = snapshot.documents.firstOrNull()?.data?.let { Tenant.fromMap(it) }
            Result.success(tenant)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Payment functions
    suspend fun createPayment(payment: Payment): Result<String> {
        return try {
            val docRef = db.collection("payments").document()
            val paymentWithId = payment.copy(id = docRef.id)
            docRef.set(paymentWithId.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPaymentsByProperty(propertyId: String): Result<List<Payment>> {
        return try {
            val snapshot = db.collection("payments")
                .whereEqualTo("propertyId", propertyId)
                .orderBy("dueDate", Query.Direction.DESCENDING)
                .get()
                .await()
            val payments = snapshot.documents.mapNotNull { it.data?.let { data -> Payment.fromMap(data) } }
            Result.success(payments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPaymentsByTenant(tenantId: String): Result<List<Payment>> {
        return try {
            val snapshot = db.collection("payments")
                .whereEqualTo("tenantId", tenantId)
                .orderBy("dueDate", Query.Direction.DESCENDING)
                .get()
                .await()
            val payments = snapshot.documents.mapNotNull { it.data?.let { data -> Payment.fromMap(data) } }
            Result.success(payments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePayment(payment: Payment): Result<Unit> {
        return try {
            db.collection("payments").document(payment.id).set(payment.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Issue functions
    suspend fun createIssue(issue: Issue): Result<String> {
        return try {
            val docRef = db.collection("issues").document()
            val issueWithId = issue.copy(id = docRef.id)
            docRef.set(issueWithId.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getIssuesByProperty(propertyId: String): Result<List<Issue>> {
        return try {
            val snapshot = db.collection("issues")
                .whereEqualTo("propertyId", propertyId)
                .orderBy("reportedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val issues = snapshot.documents.mapNotNull { it.data?.let { data -> Issue.fromMap(data) } }
            Result.success(issues)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateIssue(issue: Issue): Result<Unit> {
        return try {
            db.collection("issues").document(issue.id).set(issue.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Storage functions
    suspend fun uploadImage(uri: Uri, path: String): Result<String> {
        return try {
            val ref = storage.reference.child(path)
            ref.putFile(uri).await()
            val downloadUrl = ref.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Utility functions
    private fun generateInviteCode(): String {
        return UUID.randomUUID().toString().substring(0, 8).uppercase()
    }
}
