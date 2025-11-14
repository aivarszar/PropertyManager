package com.propertymanager.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.propertymanager.models.Issue
import com.propertymanager.models.IssuePriority
import com.propertymanager.ui.viewmodels.IssueViewModel
import com.propertymanager.ui.viewmodels.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(
    propertyId: String,
    tenantId: String,
    ownerId: String,
    issueViewModel: IssueViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(IssuePriority.MEDIUM) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val issueState by issueViewModel.issueState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        selectedImages = uris
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ziņot par problēmu") },
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
                value = title,
                onValueChange = { title = it },
                label = { Text("Nosaukums *") },
                placeholder = { Text("piemēram, Salauzts krāns") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Apraksts *") },
                placeholder = { Text("Aprakstiet problēmu detalizēti") },
                minLines = 4,
                maxLines = 8,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Prioritāte",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedPriority == IssuePriority.LOW,
                    onClick = { selectedPriority = IssuePriority.LOW },
                    label = { Text("Zema") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedPriority == IssuePriority.MEDIUM,
                    onClick = { selectedPriority = IssuePriority.MEDIUM },
                    label = { Text("Vidēja") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedPriority == IssuePriority.HIGH,
                    onClick = { selectedPriority = IssuePriority.HIGH },
                    label = { Text("Augsta") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedPriority == IssuePriority.URGENT,
                    onClick = { selectedPriority = IssuePriority.URGENT },
                    label = { Text("Steidzama") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pievienot attēlus (${selectedImages.size})")
            }

            if (selectedImages.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedImages) { uri ->
                        Card(
                            modifier = Modifier.size(100.dp)
                        ) {
                            Box {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick = {
                                        selectedImages = selectedImages - uri
                                    },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Noņemt",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (issueState.error != null) {
                Text(
                    text = issueState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val issue = Issue(
                        propertyId = propertyId,
                        tenantId = tenantId,
                        ownerId = ownerId,
                        title = title,
                        description = description,
                        priority = selectedPriority
                    )
                    issueViewModel.createIssue(issue, selectedImages) {
                        onNavigateBack()
                    }
                },
                enabled = !issueState.isLoading &&
                         title.isNotBlank() &&
                         description.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (issueState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Nosūtīt ziņojumu")
                }
            }
        }
    }
}
