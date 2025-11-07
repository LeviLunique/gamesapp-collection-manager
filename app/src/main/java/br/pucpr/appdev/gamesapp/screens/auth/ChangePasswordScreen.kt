package br.pucpr.appdev.gamesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    padding: PaddingValues,
    onDone: () -> Unit,
    vm: ChangePasswordViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val passwordsMatch = newPassword == confirmPassword && newPassword.isNotEmpty()
    val passwordsDifferent = newPassword != currentPassword && newPassword.isNotEmpty()
    val hasMinLength = newPassword.length >= Constants.Auth.PASSWORD_MIN_LENGTH
    val hasUpperCase = newPassword.any { it.isUpperCase() }
    val hasLowerCase = newPassword.any { it.isLowerCase() }
    val hasDigit = newPassword.any { it.isDigit() }
    val hasSpecialChar = newPassword.any { !it.isLetterOrDigit() }

    val isPasswordValid = hasMinLength && passwordsMatch && passwordsDifferent

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.auth_change_password_title)) },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(innerPadding)
                .padding(Constants.Auth.AUTH_SECTION_SPACING),
            verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING)
        ) {
            OutlinedTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    message = ""
                },
                label = { Text(stringResource(R.string.label_current_password)) },
                modifier = Modifier.fillMaxWidth(),
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
                }
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    message = ""
                },
                label = { Text(stringResource(R.string.label_new_password)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = stringResource(if (newPasswordVisible) R.string.cd_hide_password else R.string.cd_show_password)
                        )
                    }
                },
                isError = newPassword.isNotEmpty() && (!passwordsDifferent || !hasMinLength),
                supportingText = {
                    if (newPassword.isNotEmpty()) {
                        if (!passwordsDifferent) {
                            Text(stringResource(R.string.error_password_same_as_current), color = MaterialTheme.colorScheme.error)
                        } else if (!hasMinLength) {
                            Text(stringResource(R.string.error_password_too_short), color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    message = ""
                },
                label = { Text(stringResource(R.string.label_confirm_new_password)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
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

            if (newPassword.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(Constants.Auth.AUTH_BUTTON_SPACING)) {
                        Text(
                            text = stringResource(R.string.password_requirements_title),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(Constants.Auth.AUTH_REQUIREMENT_SPACING))

                        PasswordRequirement(stringResource(R.string.password_requirement_min_length), hasMinLength)
                        PasswordRequirement(stringResource(R.string.password_requirement_different), passwordsDifferent)

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

            if (message.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (message.contains("sucesso"))
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(Constants.Auth.AUTH_BUTTON_SPACING),
                        color = if (message.contains("sucesso"))
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Button(
                onClick = {
                    if (currentPassword.isBlank()) {
                        message = "Digite sua senha atual"
                        return@Button
                    }
                    if (newPassword.isBlank()) {
                        message = "Digite a nova senha"
                        return@Button
                    }
                    if (confirmPassword.isBlank()) {
                        message = "Confirme a nova senha"
                        return@Button
                    }
                    if (!passwordsMatch) {
                        message = "As senhas não coincidem"
                        return@Button
                    }
                    if (!passwordsDifferent) {
                        message = "A nova senha deve ser diferente da senha atual"
                        return@Button
                    }
                    if (!hasMinLength) {
                        message = "A senha deve ter pelo menos 6 caracteres"
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        val result = vm.changePassword(currentPassword, newPassword)
                        result.onSuccess { successMessage ->
                            message = successMessage
                            currentPassword = ""
                            newPassword = ""
                            confirmPassword = ""
                        }.onFailure { exception ->
                            message = exception.message ?: "Erro desconhecido"
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && isPasswordValid && currentPassword.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Constants.Auth.LOADING_INDICATOR_SIZE),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(Constants.Auth.LOADING_SPACING))
                }
                Text(stringResource(R.string.action_change_password))
            }

            Spacer(Modifier.weight(1f))

            OutlinedButton(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_back))
            }
        }
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

