package com.example.zadanie2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.zadanie2.Presentation.Detail.DetailScreen
import com.example.zadanie2.Presentation.Detail.DetailScreenContract
import com.example.zadanie2.Presentation.Main.MainScreen
import com.example.zadanie2.Presentation.Main.MainScreenContract
import com.example.zadanie2.Presentation.Navigation.NavGraph
import com.example.zadanie2.Presentation.Navigation.Screen
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext.startKoin
import com.example.zadanie2.di.provideKoinModules
import com.example.zadanie2.Presentation.Main.MainViewModel
import com.example.zadanie2.Presentation.Detail.DetailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(provideKoinModules(this@MainActivity))
        }

        setContent {
            PokemonApp()
        }
    }
}

@Composable
fun PokemonApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val mainViewModel: MainViewModel = koinViewModel()
    val detailViewModel: DetailViewModel = koinViewModel()

    val mainContract = object : MainScreenContract {
        override fun onSearch(query: String) {
            mainViewModel.onSearch(query)
        }
        override fun onSort(byName: Boolean) {
            mainViewModel.onSort(byName)
        }
        override fun onItemClick(pokemonId: Int) {
            navController.navigate(Screen.Detail.createRoute(pokemonId))
        }
    }

    val detailContract = object : DetailScreenContract {
        override fun onSaveImage(imageUrl: String) {
            detailViewModel.saveImage(context, imageUrl)
        }
    }

    NavGraph(
        navController = navController,
        mainScreenContent = {
            MainScreen(
                state = mainViewModel.state.collectAsState().value,
                contract = mainContract
            )
        },
        detailScreenContent = { pokemonId ->
            DetailScreen(
                pokemonId = pokemonId,
                viewModel = detailViewModel,
                contract = detailContract
            )
        }
    )
}