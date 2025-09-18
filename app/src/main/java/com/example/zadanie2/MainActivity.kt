package com.example.zadanie2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import com.example.zadanie2.Presentation.Navigation.NavGraph
import com.example.zadanie2.di.provideKoinModules
import com.example.zadanie2.Presentation.Main.MainScreenContract
import com.example.zadanie2.Presentation.Main.MainScreen
import com.example.zadanie2.Presentation.Detail.DetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(provideKoinModules(this@MainActivity))
        }
        setContent { PokemonApp() }
    }
}

@Composable
fun PokemonApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val mainContract = object : MainScreenContract {
        override fun onItemClick(pokemonId: Int) {
            navController.navigate("detail/$pokemonId")
        }
    }

    NavGraph(
        navController = navController,
        mainScreenContent = { MainScreen(contract = mainContract) },
        detailScreenContent = { pokemonId -> DetailScreen(pokemonId = pokemonId) }
    )
}