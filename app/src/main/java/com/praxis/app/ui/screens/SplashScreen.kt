package com.praxis.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.praxis.app.auth.AuthState

@Composable
fun SplashScreen(
    authState: AuthState,
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit,
) {
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> onNavigateToDashboard()
            is AuthState.Unauthenticated -> onNavigateToLogin()
            is AuthState.Error -> onNavigateToLogin()
            is AuthState.Loading -> { /* wait */ }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
