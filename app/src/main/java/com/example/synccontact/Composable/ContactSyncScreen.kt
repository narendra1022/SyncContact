package com.example.synccontact.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.synccontact.ViewModel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSyncScreen(viewModel: ContactViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Contact Sync App") }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        viewModel.startSync()
                    }
                ) {
                    Text(text = "Sync Contacts")
                }
                if (uiState.isSyncing) {
                    CircularProgressIndicator()
                }
                if (uiState.isSyncSuccess) {
                    Text(
                        text = "Contacts Synced Successfully!",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        items(uiState.contacts) { contact ->
                            Text(
                                text = contact.name,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

