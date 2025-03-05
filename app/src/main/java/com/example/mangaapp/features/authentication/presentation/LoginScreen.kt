package com.example.mangaapp.features.authentication.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.success) onLoginSuccess()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material.OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )
        Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.material.OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.login() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text(text = if (uiState.isLoading) "Logging in..." else "Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        uiState.error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text(text = "Don't have an account? Register")
        }
    }
}