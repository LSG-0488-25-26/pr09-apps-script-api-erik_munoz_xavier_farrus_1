package com.example.appsscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appsscript.viewmodel.GamesViewModel

@Composable
fun FavoritesScreen(viewModel: GamesViewModel) {
    val favoriteGames by viewModel.favoriteGames.observeAsState(emptyList())
    val favorites by viewModel.favorites.observeAsState(emptySet())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (favoriteGames.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No tienes juegos favoritos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pulsa el corazón en un juego para añadirlo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteGames, key = { it.nombre }) { game ->
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
