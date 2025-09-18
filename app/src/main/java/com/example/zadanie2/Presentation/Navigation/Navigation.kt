package com.example.zadanie2.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    mainScreenContent: @Composable () -> Unit,
    detailScreenContent: @Composable (Int) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            mainScreenContent()
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("pokemonId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("pokemonId") ?: 0
            detailScreenContent(id)
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail/{pokemonId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}