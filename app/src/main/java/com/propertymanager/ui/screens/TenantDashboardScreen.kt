package com.propertymanager.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.propertymanager.models.*
import com.propertymanager.ui.viewmodels.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDashboardScreen(
    user: User,
    propertyViewModel: PropertyViewModel,
    paymentViewModel: PaymentViewModel,
    issueViewModel: IssueViewModel,
    onReportIssue: (String) -> Unit,
    onLogout: () -> Unit
) {
    val propertyState by propertyViewModel.propertyState.collectAsState()
    val paymentState by paymentViewModel.paymentState.collectAsState()
    val issueState by issueViewModel.issueState.collectAsState()

    var currentTenant by remember { mutableStateOf<Tenant?>(null) }
    var currentProperty by remember { mutableStateOf<Property?>(null) }

    LaunchedEffect(user.id) {
        propertyViewModel.propertyState.collect { state ->
            // Get active tenant for current user
            val repo = com.propertymanager.repository.FirebaseRepository()
            repo.getActiveTenantForUser(user.id).fold(
                onSuccess = { tenant ->
                    currentTenant = tenant
                    tenant?.let {
                        repo.getProperty(it.propertyId).fold(
                            onSuccess = { property ->
                                currentProperty = property
                                property?.let { prop ->
                                    paymentViewModel.loadPaymentsByTenant(it.id)
                                    issueViewModel.loadIssuesByProperty(prop.id)
                                }
                            },
                            onFailure = {}
                        )
                    }
                },
                onFailure = {}
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mana īre") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Iziet")
                    }
                }
            )
        },
        floatingActionButton = {
            currentProperty?.let { property ->
                FloatingActionButton(
                    onClick = { onReportIssue(property.id) }
                ) {
                    Icon(Icons.Default.ReportProblem, contentDescription = "Ziņot par problēmu")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // User and property info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sveicināti, ${user.name}!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    currentProperty?.let { property ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = property.address,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = property.city,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Payments section
            Text(
                text = "Maksājumi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (paymentState.payments.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nav maksājumu")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(paymentState.payments) { payment ->
                        TenantPaymentCard(
                            payment = payment,
                            paymentViewModel = paymentViewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantPaymentCard(
    payment: Payment,
    paymentViewModel: PaymentViewModel
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            showConfirmDialog = true
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = payment.description,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${payment.amount} ${payment.currency}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Termiņš: ${dateFormat.format(Date(payment.dueDate))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (payment.isRecurring) {
                        Text(
                            text = "Atkārtojas katru mēnesi",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Badge(
                    containerColor = when (payment.status) {
                        PaymentStatus.CONFIRMED -> MaterialTheme.colorScheme.primaryContainer
                        PaymentStatus.AWAITING_CONFIRMATION -> MaterialTheme.colorScheme.secondaryContainer
                        PaymentStatus.PENDING -> MaterialTheme.colorScheme.tertiaryContainer
                        PaymentStatus.OVERDUE -> MaterialTheme.colorScheme.errorContainer
                    }
                ) {
                    Text(
                        text = when (payment.status) {
                            PaymentStatus.CONFIRMED -> "✓"
                            PaymentStatus.AWAITING_CONFIRMATION -> "Gaida"
                            PaymentStatus.PENDING -> "!"
                            PaymentStatus.OVERDUE -> "!!"
                        }
                    )
                }
            }

            if (payment.status == PaymentStatus.PENDING || payment.status == PaymentStatus.OVERDUE) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Augšupielādēt maksājuma apstiprinājumu")
                }
            }

            if (payment.status == PaymentStatus.AWAITING_CONFIRMATION) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Īpašnieks pārbauda jūsu maksājumu",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (payment.status == PaymentStatus.CONFIRMED) {
                Spacer(modifier = Modifier.height(8.dp))
                payment.confirmedAt?.let {
                    Text(
                        text = "Apstiprināts: ${dateFormat.format(Date(it))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    if (showConfirmDialog && selectedImageUri != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Apstiprināt augšupielādi?") },
            text = { Text("Vai vēlaties augšupielādēt šo maksājuma apstiprinājumu?") },
            confirmButton = {
                Button(
                    onClick = {
                        paymentViewModel.uploadPaymentConfirmation(
                            payment,
                            selectedImageUri!!
                        ) {
                            showConfirmDialog = false
                            selectedImageUri = null
                        }
                    }
                ) {
                    Text("Jā, augšupielādēt")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Atcelt")
                }
            }
        )
    }
}
