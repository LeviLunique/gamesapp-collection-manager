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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.focus.FocusDirection
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.model.GameStatus
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameScreen(
    padding: PaddingValues,
    onDone: () -> Unit,
    vm: CreateGameViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(Constants.Defaults.RATING) }
    var status by remember { mutableStateOf(Constants.Defaults.STATUS) }
    var expanded by remember { mutableStateOf(false) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

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
        ) {
            Text(
                if (coverUri != null)
                    stringResource(R.string.action_change_cover)
                else
                    stringResource(R.string.action_select_cover)
            )
        }

        coverUri?.let { uri ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Constants.Ui.COVER_PREVIEW_HEIGHT),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = stringResource(R.string.label_cover_preview),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(
                onClick = onDone,
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.action_cancel))
            }
            Spacer(Modifier.width(Constants.Ui.BUTTON_SPACING))
            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        vm.saveGame(title, platform, rating, status, coverUri)
                        isLoading = false
                        onDone()
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Constants.Auth.LOADING_INDICATOR_SIZE),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(Constants.Auth.LOADING_SPACING))
                }
                Text(stringResource(R.string.action_save))
            }
        }
    }
}