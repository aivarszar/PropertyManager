package com.propertymanager.models

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val role: UserRole = UserRole.TENANT,
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "email" to email,
            "name" to name,
            "role" to role.name,
            "phone" to phone,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): User {
            return User(
                id = map["id"] as? String ?: "",
                email = map["email"] as? String ?: "",
                name = map["name"] as? String ?: "",
                role = UserRole.valueOf(map["role"] as? String ?: "TENANT"),
                phone = map["phone"] as? String ?: "",
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}
