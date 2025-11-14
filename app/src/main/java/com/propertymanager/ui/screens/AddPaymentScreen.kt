package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.propertymanager.models.Payment
import com.propertymanager.models.PaymentType
import com.propertymanager.ui.viewmodels.PropertyViewModel
import com.propertymanager.ui.viewmodels.PaymentViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentScreen(
    propertyId: String,
    tenantId: String,
    ownerId: String,
    propertyViewModel: PropertyViewModel,
    paymentViewModel: PaymentViewModel,
    onNavigateBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PaymentType.RENT) }
    var isRecurring by remember { mutableStateOf(false) }
    var recurringDay by remember { mutableStateOf("1") }
    var dueDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val paymentState by paymentViewModel.paymentState.collectAsState()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dueDate.timeInMillis
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pievienot maksājumu") },
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
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Apraksts *") },
                placeholder = { Text("piemēram, Īres maksa par oktobri") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { char -> char.isDigit() || char == '.' } },
                label = { Text("Summa (EUR) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Maksājuma veids",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedType == PaymentType.RENT,
                    onClick = { selectedType = PaymentType.RENT },
                    label = { Text("Īre") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedType == PaymentType.UTILITIES,
                    onClick = { selectedType = PaymentType.UTILITIES },
                    label = { Text("Komunālie") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedType == PaymentType.OTHER,
                    onClick = { selectedType = PaymentType.OTHER },
                    label = { Text("Cits") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Termiņš: ${android.text.format.DateFormat.format("dd.MM.yyyy", dueDate)}")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Atkārtojošs maksājums")
                Switch(
                    checked = isRecurring,
                    onCheckedChange = { isRecurring = it }
                )
            }

            if (isRecurring) {
                OutlinedTextField(
                    value = recurringDay,
                    onValueChange = {
                        val day = it.filter { char -> char.isDigit() }.toIntOrNull()
                        if (day != null && day in 1..31) {
                            recurringDay = it
                        }
                    },
                    label = { Text("Mēneša diena (1-31)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (paymentState.error != null) {
                Text(
                    text = paymentState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val payment = Payment(
                        propertyId = propertyId,
                        tenantId = tenantId,
                        ownerId = ownerId,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        description = description,
                        type = selectedType,
                        dueDate = dueDate.timeInMillis,
                        isRecurring = isRecurring,
                        recurringDay = recurringDay.toIntOrNull() ?: 1
                    )
                    paymentViewModel.createPayment(payment) {
                        onNavigateBack()
                    }
                },
                enabled = !paymentState.isLoading &&
                         description.isNotBlank() &&
                         amount.toDoubleOrNull() != null &&
                         amount.toDoubleOrNull()!! > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (paymentState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Saglabāt maksājumu")
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            dueDate.timeInMillis = it
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Atcelt")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
