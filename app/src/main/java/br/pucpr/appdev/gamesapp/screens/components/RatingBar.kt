package br.pucpr.appdev.gamesapp.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants

@Composable
fun RatingBar(
    rating: Int,
    max: Int = Constants.Ui.RATING_MAX,
    starSize: Dp = Constants.Ui.RATING_STAR_SIZE,
    starSpacing: Dp = Constants.Ui.RATING_STAR_SPACING
) {
    val cdStar = stringResource(R.string.cd_rating_star)
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(max) { index ->
            val filled = index < rating
            val icon = if (filled) Icons.Filled.Star else Icons.Outlined.Star
            Icon(
                imageVector = icon,
                contentDescription = cdStar,
                tint = if (filled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(starSize)
                    .semantics { contentDescription = cdStar }
            )
            if (index != max - 1) Spacer(Modifier.width(starSpacing))
        }
    }
}