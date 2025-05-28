package com.example.zadanie2.Presentation.Detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import com.example.zadanie2.R
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    state: DetailState,
    onSaveImage: suspend (String) -> Unit
) {
    val pokemon = state.pokemon
    val coroutineScope = rememberCoroutineScope()

    if (state.isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
        return
    }

    if (state.error != null) {
        Text(text = "Error: ${state.error}", color = MaterialTheme.colorScheme.error)
        return
    }

    if (pokemon == null) {
        Text(text = "No details available")
        return
    }

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
            onClick = {
                coroutineScope.launch {
                    onSaveImage(pokemon.imageUrl)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Image")
        }
    }
}
