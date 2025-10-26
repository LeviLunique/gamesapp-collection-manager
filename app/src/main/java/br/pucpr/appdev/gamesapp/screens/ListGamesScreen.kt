package br.pucpr.appdev.gamesapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListGamesScreen(
    padding: PaddingValues,
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit,
    vm: ListGamesViewModel = viewModel()
) {
    val games by vm.games.collectAsState()

    LaunchedEffect(Unit) { vm.loadGames() }

    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = onAdd) { Text("+") } }
    ) { _ ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Meus Jogos (${games.size})", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            LazyColumn {
                items(games) { game ->
                    Card(
                        onClick = { onEdit(game.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(game.title, style = MaterialTheme.typography.titleMedium)
                            Text("${game.platform} • ★${game.rating}")
                            if (game.notes.isNotBlank()) Text(game.notes)
                            TextButton(onClick = { vm.delete(game) }) { Text("Excluir") }
                        }
                    }
                }
            }
        }
    }
}