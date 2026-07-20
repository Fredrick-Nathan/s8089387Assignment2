package com.example.s8089387assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8089387assignment2.data.model.AnimalEntity
import com.example.s8089387assignment2.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// the possible states of the dashboard screen, used to drive the UI
sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val entities: List<AnimalEntity>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadDashboard(keypass: String) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val response = repository.getDashboard(keypass)
                _uiState.value = DashboardUiState.Success(response.entities)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(e.message ?: "Failed to load dashboard")
            }
        }
    }
}