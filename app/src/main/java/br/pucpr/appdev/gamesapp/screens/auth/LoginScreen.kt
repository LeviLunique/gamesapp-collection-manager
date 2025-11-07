package br.pucpr.appdev.gamesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onGoRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onLoggedIn: () -> Unit,
    vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val isEmailValid = email.contains("@") && email.contains(".")
    val isPasswordValid = password.length >= Constants.Auth.PASSWORD_MIN_LENGTH
    val isFormValid = isEmailValid && isPasswordValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(Constants.Auth.AUTH_SCREEN_PADDING),
        verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(Constants.Auth.AUTH_SCREEN_TOP_PADDING))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(R.string.auth_app_tagline),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.auth_login_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = null
            },
            label = { Text(stringResource(R.string.label_email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = {
                if (email.isNotEmpty() && !isEmailValid) {
                    Text(stringResource(R.string.error_invalid_email), color = MaterialTheme.colorScheme.error)
                }
            }
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = null
            },
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isFormValid && !isLoading) {
                    }
                }
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(if (passwordVisible) R.string.cd_hide_password else R.string.cd_show_password)
                    )
                }
            },
            isError = password.isNotEmpty() && !isPasswordValid,
            supportingText = {
                if (password.isNotEmpty() && !isPasswordValid) {
                    Text(stringResource(R.string.error_password_too_short), color = MaterialTheme.colorScheme.error)
                }
            }
        )

        if (error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error!!,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

        Button(
            onClick = {
                if (email.isBlank()) {
                    error = "Digite seu email"
                    return@Button
                }
                if (!isEmailValid) {
                    error = "Email inválido"
                    return@Button
                }
                if (password.isBlank()) {
                    error = "Digite sua senha"
                    return@Button
                }
                if (!isPasswordValid) {
                    error = "A senha deve ter pelo menos 6 caracteres"
                    return@Button
                }

                error = null
                isLoading = true
                scope.launch {
                    try {
                        vm.login(email, password)
                        onLoggedIn()
                    } catch (t: Throwable) {
                        error = when {
                            t.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ||
                            t.message?.contains("user-not-found") == true ||
                            t.message?.contains("wrong-password") == true ->
                                "Email ou senha incorretos"
                            t.message?.contains("invalid-email") == true ->
                                "Email inválido"
                            t.message?.contains("user-disabled") == true ->
                                "Esta conta foi desativada"
                            t.message?.contains("too-many-requests") == true ->
                                "Muitas tentativas. Tente novamente mais tarde"
                            t.message?.contains("network") == true ->
                                "Erro de conexão. Verifique sua internet"
                            else -> "Erro ao fazer login: ${t.message}"
                        }
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && isFormValid
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Constants.Auth.LOADING_INDICATOR_SIZE),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(Constants.Auth.LOADING_SPACING))
            }
            Text(stringResource(R.string.action_login))
        }

        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.action_forgot_password))
        }

        Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.auth_dont_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onGoRegister) {
                Text(stringResource(R.string.action_create_account))
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}