package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.propertymanager.models.Tenant
import com.propertymanager.ui.viewmodels.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTenantScreen(
    propertyId: String,
    propertyViewModel: PropertyViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var generatedCode by remember { mutableStateOf("") }

    val propertyState by propertyViewModel.propertyState.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pievienot īrnieku") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ievadiet īrnieka informāciju. Viņš saņems uzaicinājuma kodu pa e-pastu.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Vārds, uzvārds *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-pasts *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefons *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (propertyState.error != null) {
                Text(
                    text = propertyState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    val tenant = Tenant(
                        propertyId = propertyId,
                        name = name,
                        email = email,
                        phone = phone
                    )
                    propertyViewModel.createTenant(tenant) { tenantId ->
                        // Get the generated code from the updated tenant list
                        val createdTenant = propertyViewModel.propertyState.value.tenants
                            .find { it.id == tenantId }
                        generatedCode = createdTenant?.inviteCode ?: ""
                        showSuccessDialog = true
                    }
                },
                enabled = !propertyState.isLoading &&
                         name.isNotBlank() &&
                         email.isNotBlank() &&
                         phone.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (propertyState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Saglabāt un ģenerēt kodu")
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateBack()
            },
            title = { Text("Īrnieks pievienots!") },
            text = {
                Column {
                    Text("Uzaicinājuma kods:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = generatedCode,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nosūtiet šo kodu īrniekam pa e-pastu: $email",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(generatedCode))
                        showSuccessDialog = false
                        onNavigateBack()
                    }
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kopēt kodu")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Aizvērt")
                }
            }
        )
    }
}
