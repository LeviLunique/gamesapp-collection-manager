package br.pucpr.appdev.gamesapp.screens.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.pucpr.appdev.gamesapp.R

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String = stringResource(R.string.action_confirm),
    dismissButtonText: String = stringResource(R.string.action_cancel)
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(dismissButtonText)
                }
            }
        )
    }
}

