package com.propertymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.propertymanager.models.UserRole
import com.propertymanager.ui.Screen
import com.propertymanager.ui.screens.*
import com.propertymanager.ui.theme.PropertyManagerTheme
import com.propertymanager.ui.viewmodels.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PropertyManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PropertyManagerApp()
                }
            }
        }
    }
}

@Composable
fun PropertyManagerApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val propertyViewModel: PropertyViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()
    val issueViewModel: IssueViewModel = viewModel()

    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (authState.isAuthenticated) {
            when (authState.currentUser?.role) {
                UserRole.OWNER -> Screen.OwnerDashboard.route
                UserRole.TENANT -> Screen.TenantDashboard.route
                else -> Screen.RoleSelection.route
            }
        } else {
            Screen.RoleSelection.route
        }
    ) {
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onOwnerSelected = {
                    navController.navigate(Screen.SignUp.createRoute("owner"))
                },
                onTenantSelected = {
                    navController.navigate(Screen.SignUp.createRoute("tenant"))
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLoginSuccess = {
                    val destination = when (authState.currentUser?.role) {
                        UserRole.OWNER -> Screen.OwnerDashboard.route
                        UserRole.TENANT -> Screen.TenantDashboard.route
                        else -> Screen.RoleSelection.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.RoleSelection.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.SignUp.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val roleString = backStackEntry.arguments?.getString("role") ?: "tenant"
            val role = if (roleString == "owner") UserRole.OWNER else UserRole.TENANT

            SignUpScreen(
                role = role,
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSignUpSuccess = {
                    if (role == UserRole.OWNER) {
                        navController.navigate(Screen.OwnerDashboard.route) {
                            popUpTo(Screen.RoleSelection.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.TenantRegistration.route) {
                            popUpTo(Screen.RoleSelection.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.TenantRegistration.route) {
            authState.currentUser?.let { user ->
                TenantRegistrationScreen(
                    userId = user.id,
                    propertyViewModel = propertyViewModel,
                    onNavigateBack = {
                        authViewModel.signOut()
                        navController.navigate(Screen.RoleSelection.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onRegistrationSuccess = {
                        navController.navigate(Screen.TenantDashboard.route) {
                            popUpTo(Screen.TenantRegistration.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.OwnerDashboard.route) {
            authState.currentUser?.let { user ->
                OwnerDashboardScreen(
                    user = user,
                    propertyViewModel = propertyViewModel,
                    onAddProperty = {
                        navController.navigate(Screen.AddProperty.route)
                    },
                    onPropertyClick = { propertyId ->
                        navController.navigate(Screen.PropertyDetails.createRoute(propertyId))
                    },
                    onLogout = {
                        authViewModel.signOut()
                        navController.navigate(Screen.RoleSelection.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.TenantDashboard.route) {
            authState.currentUser?.let { user ->
                TenantDashboardScreen(
                    user = user,
                    propertyViewModel = propertyViewModel,
                    paymentViewModel = paymentViewModel,
                    issueViewModel = issueViewModel,
                    onReportIssue = { propertyId ->
                        navController.navigate(Screen.ReportIssue.createRoute(propertyId))
                    },
                    onLogout = {
                        authViewModel.signOut()
                        navController.navigate(Screen.RoleSelection.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.AddProperty.route) {
            authState.currentUser?.let { user ->
                AddPropertyScreen(
                    ownerId = user.id,
                    propertyViewModel = propertyViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onPropertyAdded = { propertyId ->
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screen.PropertyDetails.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable

            PropertyDetailsScreen(
                propertyId = propertyId,
                propertyViewModel = propertyViewModel,
                paymentViewModel = paymentViewModel,
                issueViewModel = issueViewModel,
                onNavigateBack = { navController.popBackStack() },
                onAddTenant = {
                    navController.navigate(Screen.AddTenant.createRoute(propertyId))
                },
                onAddPayment = { tenantId ->
                    navController.navigate(Screen.AddPayment.createRoute(propertyId, tenantId))
                },
                onPaymentClick = { paymentId ->
                    // Handle payment click if needed
                }
            )
        }

        composable(
            route = Screen.AddTenant.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable

            AddTenantScreen(
                propertyId = propertyId,
                propertyViewModel = propertyViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddPayment.route,
            arguments = listOf(
                navArgument("propertyId") { type = NavType.StringType },
                navArgument("tenantId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
            val tenantId = backStackEntry.arguments?.getString("tenantId") ?: return@composable

            authState.currentUser?.let { user ->
                AddPaymentScreen(
                    propertyId = propertyId,
                    tenantId = tenantId,
                    ownerId = user.id,
                    propertyViewModel = propertyViewModel,
                    paymentViewModel = paymentViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Screen.ReportIssue.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable

            authState.currentUser?.let { user ->
                // Get tenant ID for current user
                ReportIssueScreen(
                    propertyId = propertyId,
                    tenantId = user.id, // This should be tenant ID, not user ID
                    ownerId = "", // Should be property owner ID
                    issueViewModel = issueViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
