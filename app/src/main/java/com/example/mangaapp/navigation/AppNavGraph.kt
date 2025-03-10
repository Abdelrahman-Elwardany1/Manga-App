package com.example.mangaapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import com.example.mangaapp.features.authentication.presentation.screens.LoginScreen
import com.example.mangaapp.features.authentication.presentation.viewmodels.LoginViewModel
import com.example.mangaapp.features.authentication.presentation.screens.RegisterScreen
import com.example.mangaapp.features.authentication.presentation.viewmodels.RegisterViewModel
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
import com.example.mangaapp.features.history.data.HistoryRepositoryImpl
import com.example.mangaapp.features.history.data.local.HistoryDatabase
import com.example.mangaapp.features.history.domain.usecases.GetMangaDetailsForHistoryUseCase
import com.example.mangaapp.features.history.presentation.screens.HistoryScreen
import com.example.mangaapp.features.history.presentation.viewmodels.HistoryViewModel
import com.example.mangaapp.features.home.presentation.screens.HomeScreen
import com.example.mangaapp.features.home.presentation.viewmodels.HomeViewModel
import com.example.mangaapp.features.profile.presentation.screens.ProfileScreen
import com.example.mangaapp.features.profile.presentation.viewmodels.ProfileViewModel
import com.example.mangaapp.features.search.data.MangaRepositoryImpl
import com.example.mangaapp.features.search.domain.usecases.GetMangaListUseCase
import com.example.mangaapp.features.search.domain.usecases.GetRandomMangaUseCase
import com.example.mangaapp.features.search.presentation.screens.SearchScreen
import com.example.mangaapp.features.search.presentation.viewmodels.SearchViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Search : Screen("search")
    object History : Screen("history")
    object Profile : Screen("profile")
    object Details : Screen("details/{mangaId}")
    object Chapters : Screen("chapters/{mangaId}")
    object Reader : Screen("reader/{chapterId}")
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val mainScreens = listOf(
    Screen.Home.route,
    Screen.Search.route,
    Screen.History.route,
    Screen.Profile.route
)

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)
    }

    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home),
        BottomNavItem("Search", Screen.Search.route, Icons.Filled.Search),
        BottomNavItem("History", Screen.History.route, Icons.Filled.List),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Filled.Person)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Transparent)
                    .consumeWindowInsets(WindowInsets.navigationBars)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route,
                    modifier = Modifier.fillMaxSize()
                ) {
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
                    composable(Screen.Home.route) {
                        val homeViewModel = remember { HomeViewModel() }
                        HomeScreen(
                            viewModel = homeViewModel,
                            onMangaClick = { mangaId -> navController.navigate("details/$mangaId") }
                        )
                    }
                    composable(Screen.Search.route) {
                        val mangaRepository = MangaRepositoryImpl()
                        val getMangaListUseCase = GetMangaListUseCase(mangaRepository)
                        val getRandomMangaUseCase = GetRandomMangaUseCase(mangaRepository)
                        val searchViewModel = remember {
                            SearchViewModel(getMangaListUseCase, getRandomMangaUseCase)
                        }
                        SearchScreen(
                            viewModel = searchViewModel,
                            onMangaClick = { mangaId -> navController.navigate("details/$mangaId") }
                        )
                    }
                    composable(Screen.History.route) {
                        val context = LocalContext.current
                        val db = HistoryDatabase.getInstance(context)
                        val historyRepository = HistoryRepositoryImpl(db.historyDao())
                        val getMangaDetailsForHistoryUseCase = GetMangaDetailsForHistoryUseCase(DetailsRepositoryImpl())
                        val historyViewModel = remember {
                            HistoryViewModel(historyRepository, getMangaDetailsForHistoryUseCase)
                        }
                        HistoryScreen(
                            viewModel = historyViewModel,
                            onMangaClick = { mangaId -> navController.navigate("details/$mangaId") }
                        )
                    }
                    composable(Screen.Profile.route) {
                        val profileViewModel = remember { ProfileViewModel() }
                        ProfileScreen(viewModel = profileViewModel)
                    }
                    composable(
                        route = Screen.Details.route,
                        arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
                        val context = LocalContext.current

                        val db = HistoryDatabase.getInstance(context)
                        val historyRepository = HistoryRepositoryImpl(db.historyDao())
                        val getMangaDetailsForHistoryUseCase = GetMangaDetailsForHistoryUseCase(DetailsRepositoryImpl())
                        val historyViewModel = remember {
                            HistoryViewModel(historyRepository, getMangaDetailsForHistoryUseCase)
                        }

                        val detailsViewModel = remember { MangaDetailsViewModel(GetMangaDetailsUseCase(DetailsRepositoryImpl())) }
                        val chapterViewModel = remember { ChapterViewModel(GetChaptersUseCase(ChapterRepositoryImpl())) }
                        val detailsUiState by detailsViewModel.uiState.collectAsState()
                        val chaptersUiState by chapterViewModel.uiState.collectAsState()

                        LaunchedEffect(mangaId) {
                            detailsViewModel.loadMangaDetails(mangaId)
                            chapterViewModel.loadChapters(mangaId)
                        }

                        if (detailsUiState.isLoading) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else if (detailsUiState.error != null) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Error: ${detailsUiState.error}")
                            }
                        } else {
                            val manga = detailsUiState.manga
                            if (manga != null) {
                                Column {
                                    MangaDetailsScreen(
                                        manga = manga,
                                        chapters = chaptersUiState.chapters,
                                        onChapterClick = { chapterId ->
                                            historyViewModel.insertHistory(manga.id)
                                            navController.navigate("reader/$chapterId")
                                        }
                                    )
                                    if (chaptersUiState.isLoading) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    composable(
                        route = Screen.Chapters.route,
                        arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
                        val chapterViewModel = remember { ChapterViewModel(GetChaptersUseCase(ChapterRepositoryImpl())) }
                        ChapterScreen(
                            viewModel = chapterViewModel,
                            mangaId = mangaId,
                            onChapterRead = { chapterId -> navController.navigate("reader/$chapterId") }
                        )
                    }
                    composable(
                        route = Screen.Reader.route,
                        arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
                        val chapterReaderViewModel = remember { ChapterReaderViewModel(GetChapterPagesUseCase(ChapterReaderRepositoryImpl())) }
                        ChapterReaderScreen(
                            chapterId = chapterId,
                            viewModel = chapterReaderViewModel
                        )
                    }
                }
            }
        }

        if (currentRoute in mainScreens) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingBottomAppBar(navController = navController, items = bottomNavItems)
            }
        }
    }
}

@Composable
fun FloatingBottomAppBar(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute in mainScreens) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    clip = true
                )
                .background(Color.Transparent)
                .graphicsLayer { clip = false },
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomAppBar(
                containerColor = Color(0xFFDA0037),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(32.dp)),
                tonalElevation = 0.dp
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (isSelected) Color.White else Color(0xFFC7BEBE)
                            )
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else Color(0xFFC7BEBE)
                            )
                        }
                    }
                }
            }
        }
    }
}