package com.example.zadanie2.Presentation.Main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.zadanie2.Domain.Pokemon
import com.example.zadanie2.Presentation.MainIntent
import com.example.zadanie2.Presentation.Main.MainState
import androidx.compose.ui.res.painterResource
import com.example.zadanie2.R

@Composable
fun MainScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { onIntent(MainIntent.Search(it)) },
            label = { Text("Поисковая строка") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = { onIntent(MainIntent.Sort(true)) }, modifier = Modifier.weight(1f)) {
                Text("Сортировка по имени")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onIntent(MainIntent.Sort(false)) }, modifier = Modifier.weight(1f)) {
                Text("Сортировка по дате")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        } else if (state.error != null) {
            Text(text = "Error: ${state.error}", color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.filteredPokemons) { pokemon ->
                    PokemonListItem(pokemon = pokemon, onClick = { onItemClick(pokemon.id) })
                }
            }
        }
    }
}

@Composable
fun PokemonListItem(pokemon: Pokemon, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(64.dp),
                error = painterResource(id = R.drawable.placeholder),
                placeholder = painterResource(id = R.drawable.placeholder)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = pokemon.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = pokemon.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}