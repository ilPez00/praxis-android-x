package com.praxis.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praxis.app.auth.AuthManager
import com.praxis.app.auth.AuthState
import com.praxis.app.network.ApiService
import com.praxis.app.network.models.ProfileUpdateRequest
import com.praxis.app.network.models.UserProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfileResponse? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: ApiService,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = api.getMyProfile()
                _uiState.value = _uiState.value.copy(profile = response.user, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun updateProfile(request: ProfileUpdateRequest) {
        val userId = currentUserId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null, saveSuccess = false)
            try {
                api.updateUserProfile(userId, request)
                loadProfile()
                _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false, error = e.message)
            }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            try {
                api.completeOnboarding()
                loadProfile()
            } catch (_: Exception) { }
        }
    }

    private fun currentUserId(): String? {
        val state = authManager.authState.value
        return if (state is AuthState.Authenticated) state.userId else null
    }
}
