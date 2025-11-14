package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.propertymanager.models.Property
import com.propertymanager.ui.viewmodels.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(
    ownerId: String,
    propertyViewModel: PropertyViewModel,
    onNavigateBack: () -> Unit,
    onPropertyAdded: (String) -> Unit
) {
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var apartmentNumber by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val propertyState by propertyViewModel.propertyState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pievienot īpašumu") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Adrese *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Pilsēta *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = floor,
                onValueChange = { floor = it },
                label = { Text("Stāvs") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apartmentNumber,
                onValueChange = { apartmentNumber = it },
                label = { Text("Dzīvokļa numurs") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Apraksts") },
                minLines = 3,
                maxLines = 5,
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
                    val property = Property(
                        ownerId = ownerId,
                        address = address,
                        city = city,
                        floor = floor,
                        apartmentNumber = apartmentNumber,
                        description = description
                    )
                    propertyViewModel.createProperty(property) { propertyId ->
                        onPropertyAdded(propertyId)
                    }
                },
                enabled = !propertyState.isLoading && address.isNotBlank() && city.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (propertyState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Saglabāt")
                }
            }
        }
    }
}
