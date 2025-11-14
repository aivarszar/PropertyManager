package com.propertymanager.ui.screens

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
fun PropertyDetailsScreen(
    propertyId: String,
    propertyViewModel: PropertyViewModel,
    paymentViewModel: PaymentViewModel,
    issueViewModel: IssueViewModel,
    onNavigateBack: () -> Unit,
    onAddTenant: () -> Unit,
    onAddPayment: (String) -> Unit,
    onPaymentClick: (String) -> Unit
) {
    val propertyState by propertyViewModel.propertyState.collectAsState()
    val paymentState by paymentViewModel.paymentState.collectAsState()
    val issueState by issueViewModel.issueState.collectAsState()

    LaunchedEffect(Unit) {
        propertyViewModel.loadProperty(propertyId)
        paymentViewModel.loadPaymentsByProperty(propertyId)
        issueViewModel.loadIssuesByProperty(propertyId)
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Īrnieki", "Maksājumi", "Problēmas")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(propertyState.selectedProperty?.address ?: "")
                        Text(
                            propertyState.selectedProperty?.city ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atpakaļ")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Warning badges
            val unresolvedIssues = issueViewModel.getUnresolvedIssuesCount()
            val pendingPayments = paymentViewModel.getPendingPaymentsCount()
            val awaitingConfirmation = paymentViewModel.getAwaitingConfirmationCount()

            if (unresolvedIssues > 0 || pendingPayments > 0 || awaitingConfirmation > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (unresolvedIssues > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$unresolvedIssues neatrisinātas problēmas")
                            }
                        }
                        if (pendingPayments > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$pendingPayments nesamaksāti maksājumi")
                            }
                        }
                        if (awaitingConfirmation > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Pending,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$awaitingConfirmation gaida apstiprinājumu")
                            }
                        }
                    }
                }
            }

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> TenantsTab(
                    tenants = propertyState.tenants,
                    onAddTenant = onAddTenant,
                    onAddPayment = onAddPayment,
                    onDeactivateTenant = { propertyViewModel.deactivateTenant(it) }
                )
                1 -> PaymentsTab(
                    payments = paymentState.payments,
                    onPaymentClick = onPaymentClick
                )
                2 -> IssuesTab(
                    issues = issueState.issues,
                    onResolveIssue = { issue ->
                        issueViewModel.updateIssueStatus(issue, IssueStatus.RESOLVED) {
                            issueViewModel.loadIssuesByProperty(propertyId)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TenantsTab(
    tenants: List<Tenant>,
    onAddTenant: () -> Unit,
    onAddPayment: (String) -> Unit,
    onDeactivateTenant: (Tenant) -> Unit
) {
    val activeTenants = tenants.filter { it.isActive }

    Column(modifier = Modifier.fillMaxSize()) {
        if (activeTenants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Nav pievienotu īrnieku")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onAddTenant) {
                        Text("Pievienot īrnieku")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activeTenants) { tenant ->
                    TenantCard(
                        tenant = tenant,
                        onAddPayment = { onAddPayment(tenant.id) },
                        onDeactivate = { onDeactivateTenant(tenant) }
                    )
                }
                item {
                    OutlinedButton(
                        onClick = onAddTenant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pievienot īrnieku")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantCard(
    tenant: Tenant,
    onAddPayment: () -> Unit,
    onDeactivate: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tenant.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = tenant.email)
                    Text(text = tenant.phone)
                    if (!tenant.isRegistered) {
                        Text(
                            text = "Kods: ${tenant.inviteCode}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Nav reģistrējies",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Izvēlne")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Pievienot maksājumu") },
                            onClick = {
                                showMenu = false
                                onAddPayment()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.AttachMoney, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Izbeigt īres līgumu") },
                            onClick = {
                                showMenu = false
                                onDeactivate()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsTab(
    payments: List<Payment>,
    onPaymentClick: (String) -> Unit
) {
    if (payments.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nav maksājumu")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(payments) { payment ->
                PaymentCard(payment = payment, onClick = { onPaymentClick(payment.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(payment: Payment, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = payment.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${payment.amount} ${payment.currency}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Termiņš: ${dateFormat.format(Date(payment.dueDate))}",
                    style = MaterialTheme.typography.bodySmall
                )
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
                        PaymentStatus.CONFIRMED -> "Apstiprināts"
                        PaymentStatus.AWAITING_CONFIRMATION -> "Gaida"
                        PaymentStatus.PENDING -> "Nepieciešams"
                        PaymentStatus.OVERDUE -> "Kavēts"
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssuesTab(
    issues: List<Issue>,
    onResolveIssue: (Issue) -> Unit
) {
    if (issues.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nav ziņotu problēmu")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(issues) { issue ->
                IssueCard(issue = issue, onResolve = { onResolveIssue(issue) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueCard(issue: Issue, onResolve: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = issue.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = issue.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = dateFormat.format(Date(issue.reportedAt)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Badge(
                    containerColor = when (issue.status) {
                        IssueStatus.RESOLVED -> MaterialTheme.colorScheme.primaryContainer
                        IssueStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondaryContainer
                        IssueStatus.REPORTED -> MaterialTheme.colorScheme.errorContainer
                    }
                ) {
                    Text(
                        text = when (issue.status) {
                            IssueStatus.RESOLVED -> "Atrisināts"
                            IssueStatus.IN_PROGRESS -> "Procesā"
                            IssueStatus.REPORTED -> "Jauns"
                        }
                    )
                }
            }
            if (issue.status != IssueStatus.RESOLVED) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onResolve,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Atzīmēt kā atrisinātu")
                }
            }
        }
    }
}
