package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.propertymanager.ui.viewmodels.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantRegistrationScreen(
    userId: String,
    propertyViewModel: PropertyViewModel,
    onNavigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    var inviteCode by remember { mutableStateOf("") }
    val propertyState by propertyViewModel.propertyState.collectAsState()

    LaunchedEffect(propertyState.selectedProperty) {
        if (propertyState.selectedProperty != null) {
            onRegistrationSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reģistrēties ar kodu") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ievadiet uzaicinājuma kodu",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Jūs saņēmāt kodu no īpašnieka pa e-pastu",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = inviteCode,
                onValueChange = { inviteCode = it.uppercase() },
                label = { Text("Uzaicinājuma kods") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (propertyState.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = propertyState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    propertyViewModel.registerTenantWithCode(inviteCode, userId) { property ->
                        onRegistrationSuccess()
                    }
                },
                enabled = !propertyState.isLoading && inviteCode.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (propertyState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Reģistrēties")
                }
            }
        }
    }
}
