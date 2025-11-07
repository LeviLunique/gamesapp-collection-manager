package br.pucpr.appdev.gamesapp.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants

@Composable
fun AccountMenu(
    expanded: Boolean,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.action_edit_profile)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE)
                )
            },
            onClick = { onDismiss(); onEditProfile() }
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.action_logout)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE)
                )
            },
            onClick = { onDismiss(); onLogout() }
        )
    }
}

