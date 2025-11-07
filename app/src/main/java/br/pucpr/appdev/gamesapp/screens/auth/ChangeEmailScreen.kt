package br.pucpr.appdev.gamesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailScreen(
    padding: PaddingValues,
    onDone: () -> Unit,
    vm: ChangeEmailViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var currentEmail by remember { mutableStateOf(vm.getCurrentEmail()) }
    var newEmail by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val isEmailValid = newEmail.contains("@") && newEmail.contains(".")
    val isFormValid = isEmailValid && currentPassword.length >= Constants.Auth.PASSWORD_MIN_LENGTH

    // String resources for validation messages
    val errorNewEmailRequired = stringResource(R.string.error_new_email_required)
    val errorInvalidEmail = stringResource(R.string.error_invalid_email)
    val errorCurrentPasswordRequired = stringResource(R.string.error_current_password_required)
    val errorUnknown = stringResource(R.string.error_unknown)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.auth_change_email_title)) },
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
            Text(
                text = stringResource(R.string.auth_current_email_label, currentEmail),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

            OutlinedTextField(
                value = newEmail,
                onValueChange = {
                    newEmail = it
                    message = ""
                },
                label = { Text(stringResource(R.string.label_new_email)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = newEmail.isNotEmpty() && !isEmailValid,
                supportingText = {
                    if (newEmail.isNotEmpty() && !isEmailValid) {
                        Text(stringResource(R.string.error_invalid_email), color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            OutlinedTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    message = ""
                },
                label = { Text(stringResource(R.string.label_password_for_confirm)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
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

            if (message.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (message.contains("sucesso") || message.contains("enviado"))
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(Constants.Auth.AUTH_BUTTON_SPACING),
                        color = if (message.contains("sucesso") || message.contains("enviado"))
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

            Button(
                onClick = {
                    if (newEmail.isBlank()) {
                        message = errorNewEmailRequired
                        return@Button
                    }
                    if (!isEmailValid) {
                        message = errorInvalidEmail
                        return@Button
                    }
                    if (currentPassword.isBlank()) {
                        message = errorCurrentPasswordRequired
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        val result = vm.changeEmail(newEmail, currentPassword)
                        result.onSuccess { successMessage ->
                            message = successMessage
                            newEmail = ""
                            currentPassword = ""
                        }.onFailure { exception ->
                            message = exception.message ?: errorUnknown
                        }
                        isLoading = false
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
                Text(stringResource(R.string.action_change_email))
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

