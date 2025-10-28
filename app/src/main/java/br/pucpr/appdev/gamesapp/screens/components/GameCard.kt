package br.pucpr.appdev.gamesapp.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.model.GameEntity
import br.pucpr.appdev.gamesapp.model.GameStatus

@Composable
fun GameCard(
    game: GameEntity,
    selectionMode: Boolean,
    selected: Boolean,
    onToggleSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val borderColor =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = Constants.Ui.ALPHA_SELECTED_BORDER)
        else Color.Transparent

    val shape = RoundedCornerShape(Constants.Ui.CARD_CORNER_RADIUS)

    val borderModifier =
        if (selected) Modifier.border(Constants.Ui.CARD_BORDER_WIDTH, borderColor, shape)
        else Modifier

    ElevatedCard(
        onClick = onEdit,
        modifier = Modifier
            .fillMaxWidth()
            .then(borderModifier)
            .clip(shape),
        shape = shape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = Constants.Ui.ALPHA_SELECTED_CONTAINER)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(Constants.Ui.CARD_CONTENT_PADDING)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectionMode) {
                    Checkbox(checked = selected, onCheckedChange = { onToggleSelect() })
                    Spacer(Modifier.width(Constants.Ui.CHECKBOX_SPACING))
                }

                Text(
                    text = game.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                val (container, label) = when (game.status) {
                    GameStatus.DONE -> Constants.Ui.STATUS_DONE_CONTAINER to Constants.Ui.STATUS_DONE_LABEL
                    GameStatus.PLAYING -> Constants.Ui.STATUS_PLAYING_CONTAINER to Constants.Ui.STATUS_PLAYING_LABEL
                    GameStatus.BACKLOG -> Constants.Ui.STATUS_BACKLOG_CONTAINER to Constants.Ui.STATUS_BACKLOG_LABEL
                }

                val statusText = when (game.status) {
                    GameStatus.BACKLOG -> stringResource(R.string.status_backlog)
                    GameStatus.PLAYING -> stringResource(R.string.status_playing)
                    GameStatus.DONE    -> stringResource(R.string.status_done)
                }

                AssistChip(
                    onClick = { },
                    label = { Text(statusText) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = container,
                        labelColor = label
                    )
                )
            }

            Spacer(Modifier.height(Constants.Ui.CARD_INTERNAL_SPACING_SM))

            Text(
                text = stringResource(R.string.label_platform_value, game.platform),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(Constants.Ui.CARD_INTERNAL_SPACING_SM))

            Column {
                Text(
                    text = stringResource(
                        R.string.label_rating_value,
                        game.rating,
                        Constants.Ui.RATING_MAX
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                RatingBar(rating = game.rating, max = Constants.Ui.RATING_MAX)
            }

            if (game.notes.isNotBlank()) {
                Spacer(Modifier.height(Constants.Ui.CARD_INTERNAL_SPACING_SM))
                Text(game.notes, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(Constants.Ui.CARD_INTERNAL_SPACING_SM))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.action_delete),
                        modifier = Modifier.size(Constants.Ui.ICON_SIZE_SMALL)
                    )
                    Spacer(Modifier.width(Constants.Ui.DELETE_ICON_TEXT_SPACING))
                    Text(text = stringResource(R.string.action_delete))
                }
            }
        }
    }
}