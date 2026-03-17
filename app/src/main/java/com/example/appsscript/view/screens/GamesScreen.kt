package com.example.appsscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appsscript.model.data.Game
import com.example.appsscript.viewmodel.GamesViewModel

@Composable
fun GamesScreen(viewModel: GamesViewModel) {
    val games by viewModel.games.observeAsState(emptyList())
    val favorites by viewModel.favorites.observeAsState(emptySet())
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val searchQuery by viewModel.searchQuery.observeAsState("")

    LaunchedEffect(Unit) {
        viewModel.loadAllGames()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Buscar por nombre, desarrollador...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.clearError(); viewModel.loadAllGames() }) {
                        Text("Reintentar")
                    }
                }
                games.isEmpty() -> Text(
                    text = if (searchQuery.isBlank()) "No hay juegos disponibles" else "Sin resultados para \"$searchQuery\"",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(games, key = { it.nombre }) { game ->
                        GameItem(
                            game = game,
                            isFavorite = game.nombre in favorites,
                            onFavoriteClick = { viewModel.toggleFavorite(game.nombre) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameItem(game: Game, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(game.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Desarrollador: ${game.desarrollador}", style = MaterialTheme.typography.bodySmall)
                Text("Publicador: ${game.publisher}", style = MaterialTheme.typography.bodySmall)
                Text("Propietarios: ${game.owners}", style = MaterialTheme.typography.bodySmall)
                Text("Precio: ${game.precio} céntimos", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos" else "Añadir a favoritos",
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
