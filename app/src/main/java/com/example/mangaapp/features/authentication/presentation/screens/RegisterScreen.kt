package com.example.mangaapp.features.authentication.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangaapp.features.authentication.presentation.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.success) {
        onRegisterSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 150.dp),
            text = "Register",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.firstName,
            onValueChange = viewModel::onFirstNameChange,
            label = { Text("First Name", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDA0037),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFDA0037),
                focusedLabelColor = Color(0xFFDA0037)
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.lastName,
            onValueChange = viewModel::onLastNameChange,
            label = { Text("Last Name", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDA0037),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFDA0037),
                focusedLabelColor = Color(0xFFDA0037)
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.nickName,
            onValueChange = viewModel::onNickNameChange,
            label = { Text("Nick Name", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDA0037),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFDA0037),
                focusedLabelColor = Color(0xFFDA0037)
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email", color = Color.Gray) },
            isError = uiState.error?.contains("email") == true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDA0037),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFDA0037),
                focusedLabelColor = Color(0xFFDA0037)
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password", color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.error?.contains("Password") == true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDA0037),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFDA0037),
                focusedLabelColor = Color(0xFFDA0037)
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirm Password", color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.error?.contains("match") == true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDA0037),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFDA0037),
                focusedLabelColor = Color(0xFFDA0037)
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.password != uiState.confirmPassword && uiState.confirmPassword.isNotEmpty()) {
            Text(
                text = "Passwords do not match",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && uiState.password == uiState.confirmPassword,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDA0037))
        ) {
            Text(
                text = if (uiState.isLoading) "Registering..." else "Register",
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        uiState.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateBack) {
            Text(text = "Already have an account? Login", color = Color.White)
        }
    }
}