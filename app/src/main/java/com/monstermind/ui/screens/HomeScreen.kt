package com.monstermind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.monstermind.data.entity.Memory
import com.monstermind.ui.components.MemoryCard
import com.monstermind.ui.viewmodels.HomeViewModel

/**
 * Home Screen - Main screen of MonsterMind.
 *
 * Displays:
 * - App title and tagline
 * - Action buttons (Save Memory, Search)
 * - Recent memories
 * - Empty state if no memories
 * - Loading state
 * - Error messages
 *
 * Navigation:
 * - Save Memory button → SaveMemoryScreen
 * - Search button → SearchScreen
 * - Memory card → MemoryDetailScreen
 *
 * Example:
 * val viewModel: HomeViewModel = hiltViewModel()
 * HomeScreen(viewModel, onNavigateToSave, onNavigateToSearch, onNavigateToDetail)
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSave: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Memory) -> Unit,
    onDeleteMemory: (Memory) -> Unit = { viewModel.deleteMemory(it) }
) {
    // Collect state from ViewModel
    val recentMemories = viewModel.recentMemories.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val memoryCount = viewModel.memoryCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Section
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "MONSTER MIND",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Your phone remembers. You don't have to.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onNavigateToSave,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Save Memory")
            }

            Button(
                onClick = onNavigateToSearch,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Error Message
        if (error.value != null) {
            Text(
                text = error.value ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }

        // Recent Memories Section
        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (recentMemories.value.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No memories yet",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Save your first screenshot to get started!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Recent Memories List
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Recent Memories",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = recentMemories.value,
                        key = { memory -> memory.id }
                    ) { memory ->
                        MemoryCard(
                            memory = memory,
                            onClick = { onNavigateToDetail(memory) },
                            onDelete = { onDeleteMemory(memory) }
                        )
                    }
                }
            }
        }
    }
}