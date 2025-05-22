package com.example.zadanie2

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import com.example.zadanie2.UII.DetailScreen
import com.example.zadanie2.Presentation.DetailViewModel
import com.example.zadanie2.UII.MainScreen
import com.example.zadanie2.Presentation.MainViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail/{pokemonId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel,
    detailViewModel: DetailViewModel,
    onSaveImage: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(
                state = mainViewModel.state.collectAsState().value,
                onIntent = mainViewModel::processIntent,
                onItemClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) }
            )
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull()
            id?.let { detailViewModel.loadPokemon(it) }
            DetailScreen(
                state = detailViewModel.state.collectAsState().value,
                onSaveImage = onSaveImage
            )
        }
    }
}