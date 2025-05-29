package com.example.zadanie2.Presentation.Detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import com.example.zadanie2.R
import com.example.zadanie2.Domain.Pokemon


@Composable
fun DetailScreen(
    pokemonId: Int,
    viewModel: DetailViewModel,
    contract: DetailScreenContract
) {
    LaunchedEffect(pokemonId) {
        viewModel.loadPokemon(pokemonId)
    }

    val state = viewModel.state.collectAsState().value
    val pokemon = state.pokemon

    when {
        state.isLoading -> CircularProgressIndicator(
            modifier = Modifier.fillMaxSize().wrapContentSize()
        )
        state.error != null -> Text(
            text = "Error: ${state.error}",
            color = MaterialTheme.colorScheme.error
        )
        pokemon == null -> Text(text = "No details available")
        else -> PokemonDetailContent(
            pokemon = pokemon,
            onSaveImage = { contract.onSaveImage(pokemon.imageUrl) }
        )
    }
}

@Composable
private fun PokemonDetailContent(
    pokemon: Pokemon,
    onSaveImage: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            error = painterResource(id = R.drawable.placeholder),
            placeholder = painterResource(id = R.drawable.placeholder)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = pokemon.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = pokemon.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSaveImage,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Image")
        }
    }
}