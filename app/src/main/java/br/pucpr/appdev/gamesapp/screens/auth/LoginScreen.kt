package br.pucpr.appdev.gamesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onGoRegister: () -> Unit,
    onLoggedIn: () -> Unit,
    vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Entrar", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Senha") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)

        Button(
            onClick = {
                error = null
                scope.launch {
                    try {
                        vm.login(email, pass)
                        onLoggedIn()
                    } catch (t: Throwable) {
                        error = t.message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Entrar") }

        TextButton(onClick = onGoRegister) { Text("Criar conta") }
    }
}