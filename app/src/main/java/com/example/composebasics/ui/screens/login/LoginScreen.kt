package com.example.composebasics.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composebasics.R

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: (String, String) -> Unit,
    onGuestClick: () -> Unit
) {

    Box {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.login_txt),
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(Modifier.height(32.dp))

                    // Email field - using uiState.email
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        label = { Text(stringResource(R.string.email)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.error != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password field - using uiState.password
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        label = { Text(stringResource(R.string.password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        isError = uiState.error != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error text
                    uiState.error?.let { errorText ->
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    val fillAllFieldsMsg = stringResource(R.string.fill_all_fields)
                    val invalidEmailMsg = stringResource(R.string.invalid_email)
                    // Login button
                    Button(
                        onClick = {
                            onLoginClick(fillAllFieldsMsg, invalidEmailMsg)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading

                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(stringResource(R.string.login_txt))
                        }
                    }

                }
            }
        }
        TextButton(
            onClick = onGuestClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 20.dp, 80.dp)
        ) {
            Text(stringResource(R.string.guest_btn))
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {

    val viewModel: LoginViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()

    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onLoginClick = { fillMsg, invalidMsg ->
            viewModel.login(
                onLoginSuccess,
                fillAllFieldsMsg = fillMsg,
                invalidEmailMsg = invalidMsg
            )
        },
        onGuestClick = onLoginSuccess
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    LoginScreenContent(
        uiState = LoginUiState(
            email = "",
            password = "",
            isLoading = false,
            error = null
        ),
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = { _, _ -> },
        onGuestClick = {}
    )
}
