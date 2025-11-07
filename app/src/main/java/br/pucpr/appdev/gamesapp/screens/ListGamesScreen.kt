package br.pucpr.appdev.gamesapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.model.GameItem
import br.pucpr.appdev.gamesapp.model.GameStatus
import br.pucpr.appdev.gamesapp.model.SortKey
import br.pucpr.appdev.gamesapp.screens.components.AccountMenu
import br.pucpr.appdev.gamesapp.screens.components.GameCard
import br.pucpr.appdev.gamesapp.screens.components.GamesOverflowMenu
import br.pucpr.appdev.gamesapp.screens.components.SearchField

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGamesScreen(
    padding: PaddingValues,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
    vm: ListGamesViewModel = viewModel()
) {
    val allGames by vm.games.collectAsState()
    LaunchedEffect(Unit) { vm.loadGames() }

    var searchOpen by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf(TextFieldValue("")) }
    var sortKey by remember { mutableStateOf(SortKey.TITLE) }
    var sortAsc by remember { mutableStateOf(true) }
    var filterStatus: GameStatus? by remember { mutableStateOf(null) }
    var selectionMode by remember { mutableStateOf(false) }
    val selectedIds = remember { mutableStateListOf<String>() }

    val visibleGames by remember(allGames, search.text, filterStatus, sortKey, sortAsc) {
        mutableStateOf(
            allGames
                .asSequence()
                .filter { g ->
                    val q = search.text.trim().lowercase()
                    (q.isBlank() || g.title.lowercase().contains(q))
                }
                .filter { g -> filterStatus?.let { g.status == it } ?: true }
                .sortedWith(
                    when (sortKey) {
                        SortKey.TITLE    -> compareBy<GameItem> { it.title.lowercase() }
                        SortKey.PLATFORM -> compareBy { it.platform.lowercase() }
                        SortKey.STATUS   -> compareBy { it.status.name }
                    }
                )
                .let { if (sortAsc) it.toList() else it.toList().asReversed() }
        )
    }

    fun toggleSelect(id: String) {
        if (id in selectedIds) selectedIds.remove(id) else selectedIds.add(id)
    }
    fun selectAllCurrent() {
        selectedIds.clear()
        selectedIds.addAll(visibleGames.mapNotNull { it.id })
    }
    fun clearSelection() { selectedIds.clear(); selectionMode = false }
    fun deleteSelected() {
        visibleGames.filter { it.id in selectedIds }.forEach { vm.delete(it) }
        clearSelection()
    }

    Scaffold(
        topBar = {
            if (selectionMode) {
                TopAppBar(
                    title = { Text(stringResource(R.string.title_selected, selectedIds.size)) },
                    navigationIcon = {
                        IconButton(onClick = { clearSelection() }) {
                            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.action_close_selection))
                        }
                    },
                    actions = {
                        IconButton(onClick = { deleteSelected() }, enabled = selectedIds.isNotEmpty()) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.action_delete_selected))
                        }
                        IconButton(onClick = { selectAllCurrent() }) {
                            Icon(Icons.Default.SelectAll, contentDescription = stringResource(R.string.action_select_all))
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = { Text(stringResource(R.string.my_games_title_plain)) },
                    navigationIcon = {
                        var accountMenuOpen by remember { mutableStateOf(false) }
                        IconButton(onClick = { accountMenuOpen = true }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.cd_open_menu))
                        }
                        AccountMenu(
                            expanded = accountMenuOpen,
                            onEditProfile = onEditProfile,
                            onLogout = onLogout,
                            onDismiss = { accountMenuOpen = false }
                        )
                    },
                    actions = {
                        IconButton(onClick = { sortAsc = !sortAsc }) {
                            Icon(
                                imageVector = if (sortAsc) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = stringResource(
                                    if (sortAsc) R.string.action_sort_desc else R.string.action_sort_asc
                                )
                            )
                        }
                        IconButton(onClick = { searchOpen = !searchOpen }) {
                            Icon(Icons.Default.Search, contentDescription = stringResource(R.string.action_search))
                        }
                        var menuOpen by remember { mutableStateOf(false) }
                        IconButton(onClick = { menuOpen = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_more_options))
                        }
                        GamesOverflowMenu(
                            expanded = menuOpen,
                            sortKey = sortKey,
                            onSortKey = { sortKey = it },
                            filterStatus = filterStatus,
                            onFilterStatus = { filterStatus = it },
                            onSelectMode = { selectionMode = true; selectedIds.clear() },
                            onDismiss = { menuOpen = false }
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.action_add))
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(inner)
                .fillMaxSize()
        ) {
            Text(
                text = pluralStringResource(R.plurals.items_added, visibleGames.size, visibleGames.size),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(horizontal = Constants.Ui.SCREEN_PADDING, vertical = Constants.Ui.SECTION_SPACING_SMALL)
            )

            if (searchOpen) {
                SearchField(
                    value = search,
                    onValueChange = { search = it },
                    onClose = { searchOpen = false; search = TextFieldValue("") }
                )
            }

            Spacer(Modifier.height(Constants.Ui.SECTION_SPACING))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Constants.Ui.SECTION_SPACING),
                contentPadding = PaddingValues(
                    start = Constants.Ui.SCREEN_PADDING,
                    end = Constants.Ui.SCREEN_PADDING,
                    bottom = Constants.Ui.FAB_LIST_SPACING
                )
            ) {
                items(visibleGames, key = { it.id ?: it.title }) { game ->
                    val gid = game.id ?: return@items
                    val selected = selectionMode && (gid in selectedIds)
                    GameCard(
                        game = game,
                        selectionMode = selectionMode,
                        selected = selected,
                        onToggleSelect = { toggleSelect(gid) },
                        onEdit = { if (selectionMode) toggleSelect(gid) else onEdit(gid) },
                        onDelete = { vm.delete(game) }
                    )
                }
            }
        }
    }
}