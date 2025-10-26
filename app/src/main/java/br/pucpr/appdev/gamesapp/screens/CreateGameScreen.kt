package br.pucpr.appdev.gamesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateGameScreen(
    padding: PaddingValues,
    onDone: () -> Unit,
    vm: CreateGameViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }

    Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
        OutlinedTextField(value = platform, onValueChange = { platform = it }, label = { Text("Plataforma") })
        Slider(value = rating.toFloat(), onValueChange = { rating = it.toInt() }, valueRange = 0f..5f, steps = 4)
        Text("Nota: $rating")

        Button(onClick = {
            vm.saveGame(title, platform, rating)
            onDone()
        }) { Text("Salvar") }
    }
}