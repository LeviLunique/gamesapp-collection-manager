package br.pucpr.appdev.gamesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun EditGameScreen(
    padding: PaddingValues,
    onDone: () -> Unit,
    vm: EditGameViewModel = viewModel()
) {
    val context = LocalContext.current
    val navBackStackEntry by vm.navControllerState.collectAsState(initial = null)
    var title by rememberSaveable { mutableStateOf("") }
    var platform by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // TODO: se quiser buscar por ID, adicione método repo.get(id) e preencha os campos.
    }

    Column(
        Modifier
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
        OutlinedTextField(value = platform, onValueChange = { platform = it }, label = { Text("Plataforma") })
        Slider(value = rating.toFloat(), onValueChange = { rating = it.toInt() }, valueRange = 0f..5f, steps = 4)
        Text("Nota: $rating")

        Button(onClick = {
            vm.saveGame(title, platform, rating)
            onDone()
        }) { Text("Salvar alterações") }

        OutlinedButton(onClick = onDone) { Text("Cancelar") }
    }
}