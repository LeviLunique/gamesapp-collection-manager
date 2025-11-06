package br.pucpr.appdev.gamesapp.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.focus.FocusDirection
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.model.GameStatus
import br.pucpr.appdev.gamesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGameScreen(
    padding: PaddingValues,
    onDone: () -> Unit,
    gameId: String?,
    vm: EditGameViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    var title by rememberSaveable { mutableStateOf("") }
    var platform by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableStateOf(0) }
    var status by rememberSaveable { mutableStateOf(GameStatus.PLAYING) }
    var oldCoverUrl by rememberSaveable { mutableStateOf("") }
    var newCoverUri by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> newCoverUri = uri }

    LaunchedEffect(gameId) {
        if (!gameId.isNullOrBlank()) {
            vm.getGameById(gameId)?.let { g ->
                title = g.title
                platform = g.platform
                rating = g.rating
                status = g.status
                oldCoverUrl = g.coverUrl
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(Constants.Ui.SCREEN_PADDING)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Constants.Ui.SECTION_SPACING)
    ) {
        OutlinedTextField(
            value = title, onValueChange = { title = it }, label = { Text(stringResource(R.string.label_title)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words, autoCorrectEnabled = true, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        OutlinedTextField(
            value = platform, onValueChange = { platform = it }, label = { Text(stringResource(R.string.label_platform)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words, autoCorrectEnabled = true, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        val statusLabel: @Composable (GameStatus) -> String = {
            when (it) {
                GameStatus.BACKLOG -> stringResource(R.string.status_backlog)
                GameStatus.PLAYING -> stringResource(R.string.status_playing)
                GameStatus.DONE    -> stringResource(R.string.status_done)
            }
        }

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                readOnly = true,
                value = statusLabel(status),
                onValueChange = {},
                label = { Text(stringResource(R.string.label_status)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                GameStatus.entries.forEach { opt ->
                    DropdownMenuItem(text = { Text(statusLabel(opt)) }, onClick = { status = opt; expanded = false })
                }
            }
        }

        Text(stringResource(R.string.label_rating, rating))
        Slider(
            value = rating.toFloat(), onValueChange = { rating = it.toInt() },
            valueRange = Constants.Ui.RATING_MIN.toFloat()..Constants.Ui.RATING_MAX.toFloat(),
            steps = Constants.Ui.RATING_STEPS
        )

        OutlinedButton(
            onClick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        ) { Text(if (newCoverUri != null) "Trocar capa (imagem selecionada)" else "Selecionar nova capa (opcional)") }

        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = onDone) { Text(stringResource(R.string.action_cancel)) }
            Spacer(Modifier.width(12.dp))
            Button(onClick = {
                if (!gameId.isNullOrBlank()) {
                    vm.updateGame(gameId, title, platform, rating, status, newCoverUri, oldCoverUrl)
                }
                onDone()
            }) { Text(stringResource(R.string.action_save_changes)) }
        }
    }
}