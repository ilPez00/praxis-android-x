package com.praxis.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.praxis.app.network.models.ProfileUpdateRequest
import com.praxis.app.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile

    var editName by remember(profile) { mutableStateOf(profile?.name ?: "") }
    var editBio by remember(profile) { mutableStateOf(profile?.bio ?: "") }
    var editCity by remember(profile) { mutableStateOf(profile?.city ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null && profile == null -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadProfile() }) { Text("Retry") }
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (profile != null) {
                        StatsRow(profile)

                        HorizontalDivider()

                        Text("Name", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )

                        Text("Bio", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                        )

                        Text("City", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = editCity,
                            onValueChange = { editCity = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.updateProfile(
                                    ProfileUpdateRequest(
                                        name = editName.ifBlank { null },
                                        bio = editBio.ifBlank { null },
                                        city = editCity.ifBlank { null },
                                    )
                                )
                            },
                            enabled = !uiState.isSaving,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Save Changes")
                            }
                        }

                        if (uiState.saveSuccess) {
                            Text(
                                "Profile updated!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }

                        if (!profile.onboardingCompleted!!) {
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.completeOnboarding() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            ) { Text("Complete Onboarding") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsRow(profile: com.praxis.app.network.models.UserProfileResponse) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Praxis Points: ${profile.praxisPoints ?: 0}", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("Streak: ${profile.currentStreak ?: 0} days", style = MaterialTheme.typography.bodyMedium)
            Text("Honor: ${profile.honorScore?.toInt() ?: 0}", style = MaterialTheme.typography.bodyMedium)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            val premium = if (profile.isPremium == true) "Premium" else "Free"
            Text(premium, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            if (profile.isAdmin == true) {
                Text("Admin", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
