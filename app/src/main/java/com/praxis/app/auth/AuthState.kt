package com.praxis.app.auth

sealed class AuthState {
    data object Loading : AuthState()
    data class Authenticated(val userId: String, val token: String) : AuthState()
    data object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}
