package com.example.mangaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mangaapp.features.authentication.data.FirebaseAuthRepositoryImpl
import com.example.mangaapp.features.authentication.domain.usecases.LoginUseCase
import com.example.mangaapp.features.authentication.domain.usecases.RegisterUseCase
import com.example.mangaapp.features.authentication.presentation.LoginScreen
import com.example.mangaapp.features.authentication.presentation.LoginViewModel
import com.example.mangaapp.features.authentication.presentation.RegisterScreen
import com.example.mangaapp.features.authentication.presentation.RegisterViewModel
import com.example.mangaapp.features.chapters.data.ChapterRepositoryImpl
import com.example.mangaapp.features.chapters.domain.usecases.GetChaptersUseCase
import com.example.mangaapp.features.chapters.presentation.screens.ChapterScreen
import com.example.mangaapp.features.chapters.presentation.viewmodels.ChapterViewModel
import com.example.mangaapp.features.details.data.DetailsRepositoryImpl
import com.example.mangaapp.features.details.domain.usecases.GetMangaDetailsUseCase
import com.example.mangaapp.features.details.presentation.screens.MangaDetailsScreen
import com.example.mangaapp.features.details.presentation.viewmodels.MangaDetailsViewModel
import com.example.mangaapp.features.search.data.MangaRepositoryImpl
import com.example.mangaapp.features.search.domain.usecases.GetMangaListUseCase
import com.example.mangaapp.features.search.presentation.screens.SearchScreen
import com.example.mangaapp.features.search.presentation.viewmodels.SearchViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Search : Screen("search")
    object Details : Screen("details/{mangaId}")
    object Chapters : Screen("chapters/{mangaId}")
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    val loginViewModel = LoginViewModel(LoginUseCase(FirebaseAuthRepositoryImpl()))
    val registerViewModel = RegisterViewModel(RegisterUseCase(FirebaseAuthRepositoryImpl()))
    val searchViewModel = SearchViewModel(GetMangaListUseCase(MangaRepositoryImpl()))

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
            SearchScreen(
                viewModel = searchViewModel,
                onMangaClick = { mangaId ->
                    navController.navigate("details/$mangaId")
                }
            )
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
            val detailsViewModel = remember {
                MangaDetailsViewModel(GetMangaDetailsUseCase(DetailsRepositoryImpl()))
            }
            MangaDetailsScreen(
                viewModel = detailsViewModel,
                mangaId = mangaId,
                onReadClick = {
                    navController.navigate("chapters/$mangaId")
                }
            )
        }
        composable(
            route = Screen.Chapters.route,
            arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
            val chapterViewModel = remember {
                ChapterViewModel(GetChaptersUseCase(ChapterRepositoryImpl()))
            }
            ChapterScreen(
                viewModel = chapterViewModel,
                mangaId = mangaId
            )
        }
    }
}