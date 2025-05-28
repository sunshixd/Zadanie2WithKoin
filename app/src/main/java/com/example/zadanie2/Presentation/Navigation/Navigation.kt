package com.example.zadanie2.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.compose.runtime.collectAsState
import com.example.zadanie2.Presentation.Detail.DetailViewModel
import com.example.zadanie2.Presentation.Main.MainViewModel
import com.example.zadanie2.Presentation.Detail.DetailScreen
import com.example.zadanie2.Presentation.Main.MainScreen
import androidx.compose.ui.platform.LocalContext
import com.example.zadanie2.Presentation.Utils.saveImageToGallery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail/{pokemonId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = koinViewModel()
            MainScreen(
                state = mainViewModel.state.collectAsState().value,
                onIntent = mainViewModel::processIntent,
                onItemClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("pokemonId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val detailViewModel: DetailViewModel = koinViewModel()
            val id = backStackEntry.arguments?.getInt("pokemonId")
            id?.let { detailViewModel.loadPokemon(it) }
            DetailScreen(
                state = detailViewModel.state.collectAsState().value,
                onSaveImage = { imageUrl ->
                    CoroutineScope(Dispatchers.IO).launch {
                        saveImageToGallery(context, imageUrl)
                    }
                }
            )
        }
    }
}