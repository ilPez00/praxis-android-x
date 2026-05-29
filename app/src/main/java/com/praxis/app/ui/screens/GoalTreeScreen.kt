package com.praxis.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.praxis.app.network.models.GoalNode
import com.praxis.app.ui.viewmodel.GoalTreeViewModel
import com.praxis.app.ui.viewmodel.GoalTreeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalTreeScreen(
    onBack: () -> Unit,
    viewModel: GoalTreeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadGoalTree()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Goal Tree") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        },
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadGoalTree() }) { Text("Retry") }
                    }
                }
            }
            else -> {
                val nodes = uiState.goalTree?.nodes ?: emptyList()
                if (nodes.isEmpty()) {
                    Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Text("No goals yet. Tap + to create one.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(nodes, key = { it.id }) { node ->
                            GoalNodeCard(
                                node = node,
                                onProgressUpdate = { progress ->
                                    viewModel.updateProgress(node.id, progress)
                                },
                                onDelete = { viewModel.deleteNode(node.id) {} },
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateGoalDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, domain, parentId ->
                viewModel.createNode(name, domain, parentId) { showCreateDialog = false }
            },
        )
    }
}

@Composable
private fun GoalNodeCard(
    node: GoalNode,
    onProgressUpdate: (Int) -> Unit,
    onDelete: () -> Unit,
) {
    val progressPct = (node.progress * 100).toInt()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = node.name, style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
            if (node.customDetails != null) {
                Text(
                    text = node.customDetails,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("${progressPct}%", style = MaterialTheme.typography.labelMedium)
                Slider(
                    value = progressPct.toFloat(),
                    onValueChange = { onProgressUpdate(it.toInt()) },
                    valueRange = 0f..100f,
                    steps = 19,
                    modifier = Modifier.weight(1f),
                )
            }
            if (node.domain != null) {
                Text(
                    text = "Domain: ${node.domain}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun CreateGoalDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, domain: String?, parentId: String?) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var domain by remember { mutableStateOf("") }
    var parentId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = domain,
                    onValueChange = { domain = it },
                    label = { Text("Domain (optional)") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = parentId,
                    onValueChange = { parentId = it },
                    label = { Text("Parent ID (optional)") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(name, domain.ifBlank { null }, parentId.ifBlank { null }) },
                enabled = name.isNotBlank(),
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
