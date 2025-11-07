package br.pucpr.appdev.gamesapp.screens.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import br.pucpr.appdev.gamesapp.R
import br.pucpr.appdev.gamesapp.base.Constants
import br.pucpr.appdev.gamesapp.screens.components.DeleteAccountDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    padding: PaddingValues,
    onChangeEmail: () -> Unit,
    onChangePassword: () -> Unit,
    onAccountDeleted: () -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val currentUser = auth.currentUser
    val currentEmail = currentUser?.email ?: ""

    var showDeleteDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.auth_edit_profile_title)) },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(innerPadding)
                .padding(Constants.Auth.AUTH_SECTION_SPACING)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING)
        ) {
            Text(
                text = stringResource(R.string.profile_account_section),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(Constants.Auth.AUTH_SECTION_SPACING)) {
                    Text(
                        text = stringResource(R.string.label_current_email),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currentEmail,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(Constants.Auth.AUTH_SMALL_SPACING))

            Text(
                text = stringResource(R.string.profile_settings_section),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ElevatedCard(
                onClick = onChangeEmail,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Constants.Auth.AUTH_SECTION_SPACING),
                    horizontalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Constants.Auth.PROFILE_ICON_SIZE)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.auth_change_email_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.profile_change_email_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            ElevatedCard(
                onClick = onChangePassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Constants.Auth.AUTH_SECTION_SPACING),
                    horizontalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Constants.Auth.PROFILE_ICON_SIZE)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.auth_change_password_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.profile_change_password_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            ElevatedCard(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Constants.Auth.AUTH_SECTION_SPACING),
                    horizontalArrangement = Arrangement.spacedBy(Constants.Auth.AUTH_SECTION_SPACING)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(Constants.Auth.PROFILE_ICON_SIZE)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.action_delete_account),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = stringResource(R.string.profile_delete_account_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            if (errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMessage!!,
                        modifier = Modifier.padding(Constants.Auth.AUTH_BUTTON_SPACING),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            OutlinedButton(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_back))
            }
        }
    }

    DeleteAccountDialog(
        showDialog = showDeleteDialog,
        isLoading = isDeleting,
        onConfirm = { password ->
            isDeleting = true
            errorMessage = null
            scope.launch {
                try {
                    if (currentUser == null) {
                        errorMessage = context.getString(R.string.error_user_not_authenticated)
                        isDeleting = false
                        showDeleteDialog = false
                        return@launch
                    }

                    val credential = EmailAuthProvider.getCredential(currentEmail, password)
                    currentUser.reauthenticate(credential).await()

                    val uid = currentUser.uid

                    val gamesSnapshot = firestore.collection(Constants.Firebase.COLLECTION_USERS)
                        .document(uid)
                        .collection(Constants.Firebase.COLLECTION_GAMES)
                        .get()
                        .await()

                    gamesSnapshot.documents.forEach { doc ->
                        doc.reference.delete().await()
                    }

                    try {
                        val coversRef = storage.reference.child(Constants.Firebase.userCoversPath(uid))
                        val coversList = coversRef.listAll().await()
                        coversList.items.forEach { it.delete().await() }
                    } catch (e: Exception) {
                    }

                    try {
                        firestore.collection(Constants.Firebase.COLLECTION_USERS)
                            .document(uid)
                            .delete()
                            .await()
                    } catch (e: Exception) {
                    }

                    currentUser.delete().await()

                    showDeleteDialog = false
                    onAccountDeleted()

                } catch (e: Exception) {
                    errorMessage = e.message ?: context.getString(R.string.error_unknown)
                    isDeleting = false
                    showDeleteDialog = false
                }
            }
        },
        onDismiss = {
            showDeleteDialog = false
            errorMessage = null
        }
    )
}
