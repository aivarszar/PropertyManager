package com.propertymanager.ui

sealed class Screen(val route: String) {
    object RoleSelection : Screen("role_selection")
    object Login : Screen("login")
    object SignUp : Screen("signup/{role}") {
        fun createRoute(role: String) = "signup/$role"
    }
    object OwnerDashboard : Screen("owner_dashboard")
    object TenantDashboard : Screen("tenant_dashboard")
    object AddProperty : Screen("add_property")
    object PropertyDetails : Screen("property_details/{propertyId}") {
        fun createRoute(propertyId: String) = "property_details/$propertyId"
    }
    object AddTenant : Screen("add_tenant/{propertyId}") {
        fun createRoute(propertyId: String) = "add_tenant/$propertyId"
    }
    object AddPayment : Screen("add_payment/{propertyId}/{tenantId}") {
        fun createRoute(propertyId: String, tenantId: String) = "add_payment/$propertyId/$tenantId"
    }
    object PaymentDetails : Screen("payment_details/{paymentId}") {
        fun createRoute(paymentId: String) = "payment_details/$paymentId"
    }
    object ReportIssue : Screen("report_issue/{propertyId}") {
        fun createRoute(propertyId: String) = "report_issue/$propertyId"
    }
    object IssueDetails : Screen("issue_details/{issueId}") {
        fun createRoute(issueId: String) = "issue_details/$issueId"
    }
    object TenantRegistration : Screen("tenant_registration")
    object Reports : Screen("reports/{propertyId}") {
        fun createRoute(propertyId: String) = "reports/$propertyId"
    }
}
