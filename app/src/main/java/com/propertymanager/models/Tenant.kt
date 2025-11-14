package com.propertymanager.models

data class Tenant(
    val id: String = "",
    val propertyId: String = "",
    val userId: String = "", // References User.id when tenant registers
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val inviteCode: String = "",
    val isRegistered: Boolean = false,
    val isActive: Boolean = true, // false when tenant moves out
    val moveInDate: Long = System.currentTimeMillis(),
    val moveOutDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "id" to id,
            "propertyId" to propertyId,
            "userId" to userId,
            "name" to name,
            "email" to email,
            "phone" to phone,
            "inviteCode" to inviteCode,
            "isRegistered" to isRegistered,
            "isActive" to isActive,
            "moveInDate" to moveInDate,
            "createdAt" to createdAt
        )
        moveOutDate?.let { map["moveOutDate"] = it }
        return map
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Tenant {
            return Tenant(
                id = map["id"] as? String ?: "",
                propertyId = map["propertyId"] as? String ?: "",
                userId = map["userId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                email = map["email"] as? String ?: "",
                phone = map["phone"] as? String ?: "",
                inviteCode = map["inviteCode"] as? String ?: "",
                isRegistered = map["isRegistered"] as? Boolean ?: false,
                isActive = map["isActive"] as? Boolean ?: true,
                moveInDate = map["moveInDate"] as? Long ?: System.currentTimeMillis(),
                moveOutDate = map["moveOutDate"] as? Long,
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}
