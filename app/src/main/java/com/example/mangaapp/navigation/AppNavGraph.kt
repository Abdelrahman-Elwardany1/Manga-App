package com.example.mangaapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mangaapp.features.authentication.data.FirebaseAuthRepositoryImpl
import com.example.mangaapp.features.authentication.domain.usecases.LoginUseCase
import com.example.mangaapp.features.authentication.domain.usecases.RegisterUseCase
import com.example.mangaapp.features.authentication.presentation.LoginScreen
import com.example.mangaapp.features.authentication.presentation.LoginViewModel
import com.example.mangaapp.features.authentication.presentation.RegisterScreen
import com.example.mangaapp.features.authentication.presentation.RegisterViewModel
import com.example.mangaapp.features.chapterreader.data.ChapterReaderRepositoryImpl
import com.example.mangaapp.features.chapterreader.domain.usecases.GetChapterPagesUseCase
import com.example.mangaapp.features.chapterreader.presentation.screens.ChapterReaderScreen
import com.example.mangaapp.features.chapterreader.presentation.viewmodels.ChapterReaderViewModel
import com.example.mangaapp.features.chapters.data.ChapterRepositoryImpl
import com.example.mangaapp.features.chapters.domain.usecases.GetChaptersUseCase
import com.example.mangaapp.features.chapters.presentation.screens.ChapterScreen
import com.example.mangaapp.features.chapters.presentation.viewmodels.ChapterViewModel
import com.example.mangaapp.features.details.data.DetailsRepositoryImpl
import com.example.mangaapp.features.details.domain.usecases.GetMangaDetailsUseCase
import com.example.mangaapp.features.details.presentation.screens.MangaDetailsScreen
import com.example.mangaapp.features.details.presentation.viewmodels.MangaDetailsViewModel
import com.example.mangaapp.features.home.presentation.screens.HomeScreen
import com.example.mangaapp.features.home.presentation.viewmodels.HomeViewModel
import com.example.mangaapp.features.search.data.MangaRepositoryImpl
import com.example.mangaapp.features.search.domain.usecases.GetMangaListUseCase
import com.example.mangaapp.features.search.presentation.screens.SearchScreen
import com.example.mangaapp.features.search.presentation.viewmodels.SearchViewModel

// Sealed class defining all routes
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")      // New Home route
    object Search : Screen("search")
    object Details : Screen("details/{mangaId}")
    object Chapters : Screen("chapters/{mangaId}")
    object Reader : Screen("reader/{chapterId}")
}

// Data class for bottom navigation items
data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    // Define bottom navigation items with separate Home and Search routes.
    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home),
        BottomNavItem("Search", Screen.Search.route, Icons.Filled.Search),
        BottomNavItem("Profile", "profile", Icons.Filled.Person)
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, items = bottomNavItems) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Authentication Routes
            composable(Screen.Login.route) {
                val loginViewModel = remember { LoginViewModel(LoginUseCase(FirebaseAuthRepositoryImpl())) }
                LoginScreen(
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onLoginSuccess = { navController.navigate(Screen.Home.route) },
                    viewModel = loginViewModel
                )
            }
            composable(Screen.Register.route) {
                val registerViewModel = remember { RegisterViewModel(RegisterUseCase(FirebaseAuthRepositoryImpl())) }
                RegisterScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onRegisterSuccess = { navController.popBackStack() },
                    viewModel = registerViewModel
                )
            }
            // Bottom Navigation Routes
            composable(Screen.Home.route) {
                val homeViewModel = remember { HomeViewModel() }
                HomeScreen(
                    viewModel = homeViewModel,
                    onMangaClick = { mangaId ->
                        navController.navigate("details/$mangaId")
                    }
                )
            }
            composable(Screen.Search.route) {
                val searchViewModel = remember { SearchViewModel(GetMangaListUseCase(MangaRepositoryImpl())) }
                SearchScreen(
                    viewModel = searchViewModel,
                    onMangaClick = { mangaId ->
                        navController.navigate("details/$mangaId")
                    }
                )
            }
            composable("profile") {
                // For now, a static profile screen
                Text(
                    "Profile Screen",
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
            // Details Route: Shows manga details along with chapter list
            composable(
                route = Screen.Details.route,
                arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
            ) { backStackEntry ->
                val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
                val detailsViewModel = remember {
                    MangaDetailsViewModel(GetMangaDetailsUseCase(DetailsRepositoryImpl()))
                }
                val chapterViewModel = remember {
                    ChapterViewModel(GetChaptersUseCase(ChapterRepositoryImpl()))
                }
                val detailsUiState by detailsViewModel.uiState.collectAsState()
                val chaptersUiState by chapterViewModel.uiState.collectAsState()

                LaunchedEffect(mangaId) {
                    detailsViewModel.loadMangaDetails(mangaId)
                    chapterViewModel.loadChapters(mangaId)
                }

                when {
                    detailsUiState.isLoading || chaptersUiState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    detailsUiState.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Error: ${detailsUiState.error}")
                        }
                    }
                    else -> {
                        val manga = detailsUiState.manga
                        val chapters = chaptersUiState.chapters
                        if (manga != null) {
                            MangaDetailsScreen(
                                manga = manga,
                                chapters = chapters,
                                onChapterClick = { chapterId ->
                                    navController.navigate("reader/$chapterId")
                                }
                            )
                        }
                    }
                }
            }
            // Chapters Route: Lists chapters for a manga
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
                    mangaId = mangaId,
                    onChapterRead = { chapterId ->
                        navController.navigate("reader/$chapterId")
                    }
                )
            }
            // Chapter Reader Route: Displays chapter pages for reading
            composable(
                route = Screen.Reader.route,
                arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
            ) { backStackEntry ->
                val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
                val chapterReaderViewModel = remember {
                    ChapterReaderViewModel(GetChapterPagesUseCase(ChapterReaderRepositoryImpl()))
                }
                ChapterReaderScreen(
                    chapterId = chapterId,
                    viewModel = chapterReaderViewModel
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: androidx.navigation.NavHostController,
    items: List<BottomNavItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}