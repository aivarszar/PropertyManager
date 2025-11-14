package com.propertymanager.models

enum class PaymentStatus {
    PENDING,
    AWAITING_CONFIRMATION,
    CONFIRMED,
    OVERDUE
}

enum class PaymentType {
    RENT,
    UTILITIES,
    OTHER
}

data class Payment(
    val id: String = "",
    val propertyId: String = "",
    val tenantId: String = "",
    val ownerId: String = "",
    val amount: Double = 0.0,
    val currency: String = "EUR",
    val type: PaymentType = PaymentType.RENT,
    val description: String = "",
    val dueDate: Long = System.currentTimeMillis(),
    val status: PaymentStatus = PaymentStatus.PENDING,
    val confirmationImageUrl: String? = null,
    val confirmedAt: Long? = null,
    val isRecurring: Boolean = false,
    val recurringDay: Int = 1, // Day of month for recurring payments
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "id" to id,
            "propertyId" to propertyId,
            "tenantId" to tenantId,
            "ownerId" to ownerId,
            "amount" to amount,
            "currency" to currency,
            "type" to type.name,
            "description" to description,
            "dueDate" to dueDate,
            "status" to status.name,
            "isRecurring" to isRecurring,
            "recurringDay" to recurringDay,
            "createdAt" to createdAt
        )
        confirmationImageUrl?.let { map["confirmationImageUrl"] = it }
        confirmedAt?.let { map["confirmedAt"] = it }
        return map
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Payment {
            return Payment(
                id = map["id"] as? String ?: "",
                propertyId = map["propertyId"] as? String ?: "",
                tenantId = map["tenantId"] as? String ?: "",
                ownerId = map["ownerId"] as? String ?: "",
                amount = (map["amount"] as? Number)?.toDouble() ?: 0.0,
                currency = map["currency"] as? String ?: "EUR",
                type = PaymentType.valueOf(map["type"] as? String ?: "RENT"),
                description = map["description"] as? String ?: "",
                dueDate = map["dueDate"] as? Long ?: System.currentTimeMillis(),
                status = PaymentStatus.valueOf(map["status"] as? String ?: "PENDING"),
                confirmationImageUrl = map["confirmationImageUrl"] as? String,
                confirmedAt = map["confirmedAt"] as? Long,
                isRecurring = map["isRecurring"] as? Boolean ?: false,
                recurringDay = (map["recurringDay"] as? Number)?.toInt() ?: 1,
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}
