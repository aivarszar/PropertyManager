package com.propertymanager.models

enum class IssueStatus {
    REPORTED,
    IN_PROGRESS,
    RESOLVED
}

enum class IssuePriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

data class Issue(
    val id: String = "",
    val propertyId: String = "",
    val tenantId: String = "",
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val priority: IssuePriority = IssuePriority.MEDIUM,
    val status: IssueStatus = IssueStatus.REPORTED,
    val imageUrls: List<String> = emptyList(),
    val reportedAt: Long = System.currentTimeMillis(),
    val resolvedAt: Long? = null
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "id" to id,
            "propertyId" to propertyId,
            "tenantId" to tenantId,
            "ownerId" to ownerId,
            "title" to title,
            "description" to description,
            "priority" to priority.name,
            "status" to status.name,
            "imageUrls" to imageUrls,
            "reportedAt" to reportedAt
        )
        resolvedAt?.let { map["resolvedAt"] = it }
        return map
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Issue {
            return Issue(
                id = map["id"] as? String ?: "",
                propertyId = map["propertyId"] as? String ?: "",
                tenantId = map["tenantId"] as? String ?: "",
                ownerId = map["ownerId"] as? String ?: "",
                title = map["title"] as? String ?: "",
                description = map["description"] as? String ?: "",
                priority = IssuePriority.valueOf(map["priority"] as? String ?: "MEDIUM"),
                status = IssueStatus.valueOf(map["status"] as? String ?: "REPORTED"),
                imageUrls = (map["imageUrls"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                reportedAt = map["reportedAt"] as? Long ?: System.currentTimeMillis(),
                resolvedAt = map["resolvedAt"] as? Long
            )
        }
    }
}
