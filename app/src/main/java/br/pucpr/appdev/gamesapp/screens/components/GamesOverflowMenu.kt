package br.pucpr.appdev.gamesapp.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.size
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.model.GameStatus
import br.pucpr.appdev.gamesapp.model.SortKey

@Composable
fun GamesOverflowMenu(
    expanded: Boolean,
    sortKey: SortKey,
    onSortKey: (SortKey) -> Unit,
    filterStatus: GameStatus?,
    onFilterStatus: (GameStatus?) -> Unit,
    onSelectMode: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {

        DropdownMenuItem(
            text = { Text(stringResource(R.string.action_select_items)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CheckBox,
                    contentDescription = null,
                    modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE)
                )
            },
            onClick = { onDismiss(); onSelectMode() }
        )

        Divider()

        Text(
            text = stringResource(R.string.menu_sort_by),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(
                    horizontal = Constants.Ui.MENU_SECTION_PAD_H,
                    vertical = Constants.Ui.MENU_SECTION_PAD_V
                )
                .alpha(Constants.Ui.MENU_SECTION_ALPHA)
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.sort_title)) },
            trailingIcon = {
                if (sortKey == SortKey.TITLE)
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE))
            },
            onClick = { onDismiss(); onSortKey(SortKey.TITLE) }
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.sort_platform)) },
            trailingIcon = {
                if (sortKey == SortKey.PLATFORM)
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE))
            },
            onClick = { onDismiss(); onSortKey(SortKey.PLATFORM) }
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.sort_status)) },
            trailingIcon = {
                if (sortKey == SortKey.STATUS)
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE))
            },
            onClick = { onDismiss(); onSortKey(SortKey.STATUS) }
        )

        Divider()

        Text(
            text = stringResource(R.string.menu_filter_by),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(
                    horizontal = Constants.Ui.MENU_SECTION_PAD_H,
                    vertical = Constants.Ui.MENU_SECTION_PAD_V
                )
                .alpha(Constants.Ui.MENU_SECTION_ALPHA)
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.filter_all)) },
            trailingIcon = {
                if (filterStatus == null)
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE))
            },
            onClick = { onDismiss(); onFilterStatus(null) }
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.status_backlog)) },
            trailingIcon = {
                if (filterStatus == GameStatus.BACKLOG)
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE))
            },
            onClick = { onDismiss(); onFilterStatus(GameStatus.BACKLOG) }
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.status_playing)) },
            trailingIcon = {
                if (filterStatus == GameStatus.PLAYING)
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE))
            },
            onClick = { onDismiss(); onFilterStatus(GameStatus.PLAYING) }
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.status_done)) },
            trailingIcon = {
                if (filterStatus == GameStatus.DONE)
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(Constants.Ui.MENU_ICON_SIZE))
            },
            onClick = { onDismiss(); onFilterStatus(GameStatus.DONE) }
        )
    }
}