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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.focus.FocusDirection
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.model.GameStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameScreen(
    padding: PaddingValues,
    onDone: () -> Unit,
    vm: CreateGameViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(Constants.Defaults.RATING) }
    var status by remember { mutableStateOf(Constants.Defaults.STATUS) }
    var expanded by remember { mutableStateOf(false) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> coverUri = uri }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(Constants.Ui.SCREEN_PADDING),
        verticalArrangement = Arrangement.spacedBy(Constants.Ui.SECTION_SPACING)
    ) {
        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { Text(stringResource(R.string.label_title)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words, autoCorrectEnabled = true, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        OutlinedTextField(
            value = platform, onValueChange = { platform = it },
            label = { Text(stringResource(R.string.label_platform)) },
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

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                readOnly = true, value = statusLabel(status), onValueChange = {},
                label = { Text(stringResource(R.string.label_status)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth()
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
            onClick = {
                picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        ) { Text(if (coverUri != null) "Trocar capa (imagem selecionada)" else "Selecionar capa (opcional)") }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = onDone) { Text(stringResource(R.string.action_cancel)) }
            Spacer(Modifier.width(12.dp))
            Button(onClick = {
                vm.saveGame(title, platform, rating, status, coverUri)
                onDone()
            }) { Text(stringResource(R.string.action_save)) }
        }
    }
}