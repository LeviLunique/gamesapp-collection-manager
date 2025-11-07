package br.pucpr.appdev.gamesapp.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants

@Composable
fun DeleteAccountDialog(
    showDialog: Boolean,
    onConfirm: (password: String) -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    password = ""
                    onDismiss()
                }
            },
            title = {
                Text(
                    stringResource(R.string.dialog_confirm_delete_account_title),
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING)
                ) {
                    Text(stringResource(R.string.dialog_confirm_delete_account_message))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.label_password)) },
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
                        },
                        enabled = !isLoading
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (password.isNotBlank()) {
                            onConfirm(password)
                            password = ""
                        }
                    },
                    enabled = !isLoading && password.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Constants.Auth.LOADING_INDICATOR_SIZE),
                            color = MaterialTheme.colorScheme.onError
                        )
                        Spacer(Modifier.width(Constants.Auth.LOADING_SPACING))
                    }
                    Text(stringResource(R.string.action_delete_account))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        password = ""
                        onDismiss()
                    },
                    enabled = !isLoading
                ) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }
}

