package com.praxis.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praxis.app.auth.AuthManager
import com.praxis.app.auth.AuthState
import com.praxis.app.network.ApiService
import com.praxis.app.network.models.DashboardSummaryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val summary: DashboardSummaryResponse? = null,
    val userId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val api: ApiService,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        val state = authManager.authState.value
        if (state is AuthState.Authenticated) {
            _uiState.value = _uiState.value.copy(userId = state.userId)
            loadSummary()
        }
    }

    fun loadSummary() {
        val userId = _uiState.value.userId ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val summary = api.getDashboardSummary(userId)
                _uiState.value = _uiState.value.copy(summary = summary, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
