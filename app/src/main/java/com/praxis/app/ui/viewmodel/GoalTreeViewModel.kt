package com.praxis.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praxis.app.auth.AuthManager
import com.praxis.app.network.ApiService
import com.praxis.app.network.models.CreateGoalNodeRequest
import com.praxis.app.network.models.CreateGoalNodeResponse
import com.praxis.app.network.models.DeleteGoalNodeResponse
import com.praxis.app.network.models.GoalNode
import com.praxis.app.network.models.GoalTreeResponse
import com.praxis.app.network.models.ProgressUpdateRequest
import com.praxis.app.network.models.ProgressUpdateResponse
import com.praxis.app.network.models.UpdateGoalNodeRequest
import com.praxis.app.network.models.UpdateGoalNodeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalTreeUiState(
    val goalTree: GoalTreeResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class GoalTreeViewModel @Inject constructor(
    private val api: ApiService,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalTreeUiState())
    val uiState: StateFlow<GoalTreeUiState> = _uiState.asStateFlow()

    fun loadGoalTree() {
        val userId = authManager.getAccessToken()?.let {
            val state = authManager.authState.value
            if (state is com.praxis.app.auth.AuthState.Authenticated) state.userId else return
        } ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val tree = api.getGoalTree(userId)
                _uiState.value = _uiState.value.copy(goalTree = tree, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun updateProgress(nodeId: String, progress: Int) {
        val userId = currentUserId() ?: return
        viewModelScope.launch {
            try {
                api.updateNodeProgress(userId, nodeId, ProgressUpdateRequest(progress))
                loadGoalTree()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun createNode(name: String, domain: String?, parentId: String?, onSuccess: (CreateGoalNodeResponse) -> Unit) {
        val userId = currentUserId() ?: return
        viewModelScope.launch {
            try {
                val response = api.createGoalNode(userId, CreateGoalNodeRequest(name = name, domain = domain, parentId = parentId))
                loadGoalTree()
                onSuccess(response)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateNode(nodeId: String, request: UpdateGoalNodeRequest, onSuccess: (UpdateGoalNodeResponse) -> Unit) {
        val userId = currentUserId() ?: return
        viewModelScope.launch {
            try {
                val response = api.updateGoalNode(userId, nodeId, request)
                loadGoalTree()
                onSuccess(response)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteNode(nodeId: String, onSuccess: (DeleteGoalNodeResponse) -> Unit) {
        val userId = currentUserId() ?: return
        viewModelScope.launch {
            try {
                val response = api.deleteGoalNode(userId, nodeId)
                loadGoalTree()
                onSuccess(response)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    private fun currentUserId(): String? {
        val state = authManager.authState.value
        return if (state is com.praxis.app.auth.AuthState.Authenticated) state.userId else null
    }
}
