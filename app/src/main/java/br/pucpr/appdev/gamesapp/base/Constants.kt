package br.pucpr.appdev.gamesapp.base

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.gamesapp.model.GameStatus

class Constants {
    object Nav {
        const val ARG_ID = "id"
    }

    object Ui {
        val SCREEN_PADDING = 16.dp
        val SECTION_SPACING = 12.dp
        val FAB_LIST_SPACING = 80.dp

        val CARD_CONTENT_PADDING = 16.dp
        val CARD_INTERNAL_SPACING_SM = 8.dp
        val DELETE_ICON_TEXT_SPACING = 6.dp
        val CHECKBOX_SPACING = 8.dp
        val CARD_BORDER_WIDTH = 1.dp
        val CARD_CORNER_RADIUS = 12.dp
        val CARD_BORDER_WIDTH_SELECTED = 2.dp

        const val RATING_MIN = 0
        const val RATING_MAX = 5
        const val RATING_STEPS = 4
        val RATING_STAR_SIZE = 18.dp
        val RATING_STAR_SPACING = 2.dp

        const val ALPHA_SELECTED_CONTAINER = 0.12f
        const val ALPHA_SELECTED_BORDER = 0.40f

        val ICON_SIZE_SMALL = 18.dp

        val MENU_SECTION_PAD_H = 16.dp
        val MENU_SECTION_PAD_V = 6.dp
        const val MENU_SECTION_ALPHA = 0.7f
        val MENU_ICON_SIZE = 20.dp

        val STATUS_DONE_CONTAINER = Color(0xFFE6F4EA)
        val STATUS_DONE_LABEL = Color(0xFF1E8E3E)

        val STATUS_PLAYING_CONTAINER = Color(0xFFE8F0FE)
        val STATUS_PLAYING_LABEL = Color(0xFF1A73E8)

        val STATUS_BACKLOG_CONTAINER = Color(0xFFFFF4E5)
        val STATUS_BACKLOG_LABEL = Color(0xFFEA8600)
    }

    object Defaults {
        val STATUS: GameStatus = GameStatus.PLAYING
        const val RATING = 0
    }
}