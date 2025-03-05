package com.example.mangaapp.navigation

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
import com.example.mangaapp.features.search.data.MangaRepositoryImpl
import com.example.mangaapp.features.search.domain.usecases.GetMangaListUseCase
import com.example.mangaapp.features.search.presentation.screens.SearchScreen
import com.example.mangaapp.features.search.presentation.viewmodels.SearchViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Search : Screen("search")
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val loginViewModel = remember { LoginViewModel(LoginUseCase(FirebaseAuthRepositoryImpl())) }
    val registerViewModel = remember { RegisterViewModel(RegisterUseCase(FirebaseAuthRepositoryImpl())) }
    val searchViewModel = remember {
        SearchViewModel(GetMangaListUseCase(MangaRepositoryImpl()))
    }
    NavHost(navController = navController, startDestination = Screen.Search.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { navController.navigate(Screen.Search.route) },
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
        composable(Screen.Search.route) {
            SearchScreen(viewModel = searchViewModel)
        }
    }
}