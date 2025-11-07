package br.pucpr.appdev.gamesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
fun RegisterScreen(
    onGoLogin: () -> Unit,
    onRegistered: () -> Unit,
    vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val isEmailValid = email.contains("@") && email.contains(".")
    val passwordsMatch = password == confirmPassword && password.isNotEmpty()
    val hasMinLength = password.length >= Constants.Auth.PASSWORD_MIN_LENGTH
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }

    val isFormValid = isEmailValid && hasMinLength && passwordsMatch

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(Constants.Auth.AUTH_SCREEN_PADDING),
        verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING)
    ) {
        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.auth_register_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(R.string.auth_register_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
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
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(if (passwordVisible) R.string.cd_hide_password else R.string.cd_show_password)
                    )
                }
            },
            isError = password.isNotEmpty() && !hasMinLength,
            supportingText = {
                if (password.isNotEmpty() && !hasMinLength) {
                    Text(stringResource(R.string.error_password_too_short), color = MaterialTheme.colorScheme.error)
                }
            }
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(R.string.label_confirm_password)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(if (confirmPasswordVisible) R.string.cd_hide_password else R.string.cd_show_password)
                    )
                }
            },
            isError = confirmPassword.isNotEmpty() && !passwordsMatch,
            supportingText = {
                if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                    Text(stringResource(R.string.error_passwords_dont_match), color = MaterialTheme.colorScheme.error)
                }
            }
        )

        if (password.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = stringResource(R.string.password_requirements_title),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(Constants.Auth.AUTH_REQUIREMENT_SPACING))

                    PasswordRequirement(stringResource(R.string.password_requirement_min_length), hasMinLength)

                    Text(
                        text = stringResource(R.string.password_requirements_recommended),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Constants.Auth.AUTH_REQUIREMENT_SPACING)
                    )
                    PasswordRequirement(stringResource(R.string.password_requirement_uppercase), hasUpperCase, optional = true)
                    PasswordRequirement(stringResource(R.string.password_requirement_lowercase), hasLowerCase, optional = true)
                    PasswordRequirement(stringResource(R.string.password_requirement_digit), hasDigit, optional = true)
                    PasswordRequirement(stringResource(R.string.password_requirement_special), hasSpecialChar, optional = true)
                }
            }
        }

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
                    error = "Digite uma senha"
                    return@Button
                }
                if (!hasMinLength) {
                    error = "A senha deve ter pelo menos 6 caracteres"
                    return@Button
                }
                if (confirmPassword.isBlank()) {
                    error = "Confirme sua senha"
                    return@Button
                }
                if (!passwordsMatch) {
                    error = "As senhas não coincidem"
                    return@Button
                }

                error = null
                isLoading = true
                scope.launch {
                    try {
                        vm.register(email, password)
                        onRegistered()
                    } catch (t: Throwable) {
                        error = when {
                            t.message?.contains("email-already-in-use") == true ->
                                "Este email já está em uso"
                            t.message?.contains("invalid-email") == true ->
                                "Email inválido"
                            t.message?.contains("weak-password") == true ->
                                "Senha muito fraca. Use uma senha mais forte"
                            t.message?.contains("network") == true ->
                                "Erro de conexão. Verifique sua internet"
                            else -> "Erro ao criar conta: ${t.message}"
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
            Text(stringResource(R.string.action_register))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.auth_already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onGoLogin) {
                Text(stringResource(R.string.action_login))
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun PasswordRequirement(
    text: String,
    satisfied: Boolean,
    optional: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = if (satisfied) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (satisfied) {
                MaterialTheme.colorScheme.primary
            } else if (optional) {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = Constants.Auth.PASSWORD_REQUIREMENT_OPTIONAL_ALPHA)
            } else {
                MaterialTheme.colorScheme.error
            },
            modifier = Modifier.size(Constants.Auth.PASSWORD_REQUIREMENT_ICON_SIZE)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (satisfied) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else if (optional) {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = Constants.Auth.PASSWORD_REQUIREMENT_TEXT_ALPHA)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}