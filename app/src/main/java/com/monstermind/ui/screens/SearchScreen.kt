package com.monstermind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.monstermind.data.entity.Memory
import com.monstermind.ui.components.SearchResultCard
import com.monstermind.ui.viewmodels.SearchViewModel

/**
 * Search Screen - Find saved memories.
 *
 * Features:
 * - Natural-language search queries
 * - Real-time search as user types
 * - Relevance-ranked results
 * - Shows matched text snippet
 * - No internet required (local search)
 *
 * Examples:
 * "concert" - finds all memories with "concert" text
 * "gaming" - finds gaming category memories
 * "laptop 2026" - finds shopping/tech memories from 2026
 * "DSA notes" - finds education memories about algorithms
 *
 * Powered by:
 * - MemorySearchEngine for intelligent ranking
 * - CategoryClassifier for category awareness
 * - Natural tokenization and keyword matching
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Memory) -> Unit
) {
    // Collect UI state
    val query = viewModel.query.collectAsState()
    val searchResults = viewModel.searchResults.collectAsState()
    val isSearching = viewModel.isSearching.collectAsState()
    val error = viewModel.error.collectAsState()
    val resultMessage = viewModel.resultMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Memories") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Search Bar
            SearchBar(
                query = query.value,
                onQueryChange = { viewModel.updateQuery(it) },
                onSearch = { /* Search happens on every character */ },
                active = true,
                onActiveChange = { /* Keep search bar always visible */ },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search your memories...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.value.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearSearch() }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            ) {
                // Search bar content (empty)
            }

            // Result message
            Text(
                text = resultMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            )

            // Error message
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

            // Loading state
            if (isSearching.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // Empty query state
            else if (query.value.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "🔍",
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            "Start typing to search",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            "Try: 'concert', 'gaming', 'travel'",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            // No results state
            else if (searchResults.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "No memories found",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Try a different search query",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            // Results list
            else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = searchResults.value,
                        key = { result -> result.memory.id }
                    ) { result ->
                        SearchResultCard(
                            result = result,
                            onClick = { onNavigateToDetail(result.memory) }
                        )
                    }
                }
            }
        }
    }
}