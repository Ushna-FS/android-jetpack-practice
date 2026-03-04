package com.example.composebasics.ui.screens.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Define data class INSIDE ViewModel file
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    // Single StateFlow for entire UI state
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Update functions using copy (immutable updates)
    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun login(
        onSuccess: () -> Unit,
        fillAllFieldsMsg: String,
        invalidEmailMsg: String
    ) = viewModelScope.launch {
        // Start loading
        _uiState.update { it.copy(isLoading = true, error = null) }

        delay(1000) // Simulate API call

        // Validate using let/run scope functions
        val isValid = uiState.value.run {
            when {
                email.isEmpty() || password.isEmpty() -> {
                    _uiState.update { it.copy(error = fillAllFieldsMsg) }
                    false
                }

                !email.contains("@") -> {
                    _uiState.update { it.copy(error = invalidEmailMsg) }
                    false
                }

                else -> true
            }
        }

        _uiState.update { it.copy(isLoading = false) }

        if (isValid) onSuccess()
    }
}