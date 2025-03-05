package com.example.mangaapp.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mangaapp.features.authentication.data.FirebaseAuthRepositoryImpl
import com.example.mangaapp.features.authentication.domain.usecases.LoginUseCase
import com.example.mangaapp.features.authentication.domain.usecases.RegisterUseCase
import com.example.mangaapp.features.authentication.presentation.LoginScreen
import com.example.mangaapp.features.authentication.presentation.LoginViewModel
import com.example.mangaapp.features.authentication.presentation.RegisterScreen
import com.example.mangaapp.features.authentication.presentation.RegisterViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val loginViewModel = remember { LoginViewModel(LoginUseCase(FirebaseAuthRepositoryImpl())) }
    val registerViewModel = remember { RegisterViewModel(RegisterUseCase(FirebaseAuthRepositoryImpl())) }
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { Toast.makeText(navController.context, "Login successful", Toast.LENGTH_SHORT).show() },
                viewModel = loginViewModel
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.popBackStack() },
                viewModel = registerViewModel
            )
        }
    }
}