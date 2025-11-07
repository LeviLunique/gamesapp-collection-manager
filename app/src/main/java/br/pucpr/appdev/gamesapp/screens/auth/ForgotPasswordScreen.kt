package br.pucpr.appdev.gamesapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    vm: ForgotPasswordViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isEmailValid = email.contains("@") && email.contains(".")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.auth_forgot_password_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(Constants.Auth.AUTH_SCREEN_PADDING),
            verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!success) {
                Spacer(Modifier.height(Constants.Auth.AUTH_SECTION_SPACING))

                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(Constants.Auth.ICON_SIZE_LARGE),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

                Text(
                    text = stringResource(R.string.auth_forgot_password_question),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.auth_forgot_password_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(Constants.Auth.AUTH_SECTION_SPACING))

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
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isEmailValid && !isLoading) {
                            }
                        }
                    ),
                    isError = email.isNotEmpty() && !isEmailValid,
                    supportingText = {
                        if (email.isNotEmpty() && !isEmailValid) {
                            Text(stringResource(R.string.error_invalid_email), color = MaterialTheme.colorScheme.error)
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

                        error = null
                        isLoading = true
                        scope.launch {
                            val result = vm.sendPasswordResetEmail(email)
                            result.onSuccess {
                                success = true
                            }.onFailure { exception ->
                                error = exception.message
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && isEmailValid
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Constants.Auth.LOADING_INDICATOR_SIZE),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(Constants.Auth.LOADING_SPACING))
                    }
                    Text(stringResource(R.string.action_send_reset_link))
                }

                Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

                TextButton(onClick = onBack) {
                    Text(stringResource(R.string.action_back_to_login))
                }

            } else {
                Spacer(Modifier.height(32.dp))

                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(Constants.Auth.ICON_SIZE_EXTRA_LARGE),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(Constants.Auth.AUTH_SECTION_SPACING))

                Text(
                    text = stringResource(R.string.auth_email_sent_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Constants.Auth.AUTH_SECTION_SPACING),
                        verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SMALL_SPACING)
                    ) {
                        Text(
                            text = stringResource(R.string.auth_verification_email_sent),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = email,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.auth_check_inbox),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_REQUIREMENT_SPACING)
                    ) {
                        Text(
                            text = stringResource(R.string.password_tips_title),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.password_tip_check_spam),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.password_tip_link_expiry),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.password_tip_can_resend),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(Constants.Auth.AUTH_SECTION_SPACING))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.action_back_to_login))
                }

                Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

                TextButton(
                    onClick = {
                        success = false
                        email = ""
                        error = null
                    }
                ) {
                    Text(stringResource(R.string.action_send_to_another_email))
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

