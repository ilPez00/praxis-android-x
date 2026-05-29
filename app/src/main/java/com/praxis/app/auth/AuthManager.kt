package com.praxis.app.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class Session(
    val userId: String,
    val accessToken: String,
    val email: String? = null,
)

sealed class AuthResult {
    data class Success(val session: Session) : AuthResult()
    data class Failure(val message: String) : AuthResult()
}

sealed class Either {
    data class Left(val value: AuthResult.Failure) : Either()
    data class Right(val value: Session) : Either()
}

inline fun Either.onLeft(action: (AuthResult.Failure) -> Unit) {
    if (this is Either.Left) action(value)
}

inline fun Either.onRight(action: (Session) -> Unit) {
    if (this is Either.Right) action(value)
}

@Singleton
class AuthManager @Inject constructor(
    private val supabase: SupabaseClient,
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _session = MutableStateFlow<Session?>(null)
    val session: StateFlow<Session?> = _session.asStateFlow()

    init {
        when (val status = supabase.auth.sessionStatus.value) {
            is SessionStatus.Authenticated -> {
                val s = status.session
                val session = Session(
                    userId = s.user?.id ?: "",
                    accessToken = s.accessToken,
                    email = s.user?.email,
                )
                _session.value = session
                _authState.value = AuthState.Authenticated(session.userId, session.accessToken)
            }
            is SessionStatus.NotAuthenticated -> {
                _authState.value = AuthState.Unauthenticated
            }
            else -> {
                _authState.value = AuthState.Loading
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onResult: (Either) -> Unit,
    ) {
        _authState.value = AuthState.Loading
        scope.launch {
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                val status = supabase.auth.sessionStatus.value
                if (status is SessionStatus.Authenticated) {
                    val s = status.session
                    val session = Session(
                        userId = s.user?.id ?: "",
                        accessToken = s.accessToken,
                        email = s.user?.email,
                    )
                    _session.value = session
                    _authState.value = AuthState.Authenticated(session.userId, session.accessToken)
                    onResult(Either.Right(session))
                } else {
                    val msg = "Login succeeded but no session was returned"
                    _authState.value = AuthState.Error(msg)
                    onResult(Either.Left(AuthResult.Failure(msg)))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val message = e.message ?: "Login failed"
                _authState.value = AuthState.Error(message)
                onResult(Either.Left(AuthResult.Failure(message)))
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        onResult: (Either) -> Unit,
    ) {
        _authState.value = AuthState.Loading
        scope.launch {
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                val status = supabase.auth.sessionStatus.value
                if (status is SessionStatus.Authenticated) {
                    val s = status.session
                    val session = Session(
                        userId = s.user?.id ?: "",
                        accessToken = s.accessToken,
                        email = s.user?.email,
                    )
                    _session.value = session
                    _authState.value = AuthState.Authenticated(session.userId, session.accessToken)
                    onResult(Either.Right(session))
                } else {
                    val msg = "Check your email for a confirmation link"
                    _authState.value = AuthState.Unauthenticated
                    onResult(Either.Left(AuthResult.Failure(msg)))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val message = e.message ?: "Sign up failed"
                _authState.value = AuthState.Error(message)
                onResult(Either.Left(AuthResult.Failure(message)))
            }
        }
    }

    fun logout() {
        scope.launch {
            try {
                supabase.auth.signOut()
            } catch (_: Exception) { }
            _session.value = null
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun getAccessToken(): String? {
        val status = supabase.auth.sessionStatus.value
        return if (status is SessionStatus.Authenticated) status.session.accessToken else null
    }
}
