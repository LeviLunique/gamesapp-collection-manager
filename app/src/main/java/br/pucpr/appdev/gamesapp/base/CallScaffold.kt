package br.pucpr.appdev.gamesapp.base

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallScaffold(
    topTitle: String,
    content: @Composable (PaddingValues) -> Unit
) {
    val (paddingValues, setPadding) = remember { mutableStateOf(PaddingValues()) }
    Scaffold(
    ) { padding ->
        setPadding(padding)
        content(padding)
    }
}