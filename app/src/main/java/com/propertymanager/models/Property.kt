package com.propertymanager.models

data class Property(
    val id: String = "",
    val ownerId: String = "",
    val address: String = "",
    val city: String = "",
    val floor: String = "",
    val apartmentNumber: String = "",
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "ownerId" to ownerId,
            "address" to address,
            "city" to city,
            "floor" to floor,
            "apartmentNumber" to apartmentNumber,
            "description" to description,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Property {
            return Property(
                id = map["id"] as? String ?: "",
                ownerId = map["ownerId"] as? String ?: "",
                address = map["address"] as? String ?: "",
                city = map["city"] as? String ?: "",
                floor = map["floor"] as? String ?: "",
                apartmentNumber = map["apartmentNumber"] as? String ?: "",
                description = map["description"] as? String ?: "",
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}
