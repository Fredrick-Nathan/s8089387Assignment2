package com.example.s8089387assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8089387assignment2.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// the possible states of the login screen, used to drive the UI
sealed class LoginUiState {
    // nothing has happened yet
    data object Idle : LoginUiState()
    // request is in flight
    data object Loading : LoginUiState()
    // login succeeded, carries the keypass needed for the Dashboard screen
    data class Success(val keypass: String) : LoginUiState()
    // login failed, carries a message to show the user
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    // backing field the ViewModel can mutate
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    // read-only field the Activity observes
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // called when the user taps the login button
    fun login(username: String, password: String) {
        // basic empty-field check before hitting the network
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Username and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = repository.login(username, password)
                _uiState.value = LoginUiState.Success(response.keypass)
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Login failed. Please try again.")
            }
        }
    }
}