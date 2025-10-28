package br.pucpr.appdev.gamesapp.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.R

@Composable
public fun SearchField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClose: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            if (value.text.isNotEmpty()) {
                IconButton(onClick = { onValueChange(TextFieldValue("")) }) { Icon(Icons.Default.Clear, null) }
            } else {
                IconButton(onClick = onClose) { Icon(Icons.Default.Close, null) }
            }
        },
        placeholder = { Text(stringResource(R.string.hint_search_game)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Constants.Ui.SCREEN_PADDING)
    )
}