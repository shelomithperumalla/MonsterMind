package com.monstermind.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monstermind.data.entity.MemorySearchResult
import com.monstermind.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Search Screen.
 *
 * Manages:
 * - Search query input
 * - Search results
 * - Result ranking
 * - Empty state handling
 * - Error handling
 *
 * Search Features:
 * - Natural-language queries ("Find my concert poster")
 * - Keyword-based search ("Valorant")
 * - Category filtering
 * - Relevance scoring
 * - No external API calls (fully local)
 *
 * Example:
 * viewModel.updateQuery("concert")
 * val results = viewModel.searchResults.value
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel() {

    // Search state
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MemorySearchResult>>(emptyList())
    val searchResults: StateFlow<List<MemorySearchResult>> = _searchResults.asStateFlow()

    private val _resultCount = MutableStateFlow(0)
    val resultCount: StateFlow<Int> = _resultCount.asStateFlow()

    // Loading and error states
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Update search query and perform search.
     *
     * Performs search after each character input.
     * Debouncing could be added for performance if needed.
     *
     * @param newQuery The new search query
     */
    fun updateQuery(newQuery: String) {
        _query.value = newQuery

        if (newQuery.isBlank()) {
            _searchResults.value = emptyList()
            _resultCount.value = 0
            _error.value = null
            return
        }

        performSearch(newQuery)
    }

    /**
     * Perform the actual search using repository.
     *
     * @param query The search query
     */
    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                _isSearching.value = true
                _error.value = null

                // Search using repository
                val results = repository.searchMemories(query)

                _searchResults.value = results
                _resultCount.value = results.size

            } catch (e: Exception) {
                _error.value = "Search failed: ${e.message}"
                _searchResults.value = emptyList()
                _resultCount.value = 0
                e.printStackTrace()
            } finally {
                _isSearching.value = false
            }
        }
    }

    /**
     * Clear search and results.
     */
    fun clearSearch() {
        _query.value = ""
        _searchResults.value = emptyList()
        _resultCount.value = 0
        _error.value = null
    }

    /**
     * Clear error message.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Get search result message for display.
     *
     * Examples:
     * "3 results found"
     * "No results found"
     * "Search your memories..."
     */
    val resultMessage: String
        get() {
            return when {
                query.value.isBlank() -> "Search your memories..."
                resultCount.value == 0 -> "No results found"
                resultCount.value == 1 -> "1 result found"
                else -> "${resultCount.value} results found"
            }
        }
}