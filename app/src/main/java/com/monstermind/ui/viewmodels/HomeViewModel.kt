package com.monstermind.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monstermind.data.entity.Memory
import com.monstermind.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Home Screen.
 *
 * Manages:
 * - Recent memories display
 * - Memory count
 * - Memory deletion
 * - UI state (loading, error)
 *
 * Uses:
 * - Repository for data access
 * - StateFlow for reactive updates
 * - Coroutines for async operations
 *
 * Example:
 * val recentMemories = viewModel.recentMemories.value
 * viewModel.deleteMemory(memory)
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel() {

    // UI State
    private val _recentMemories = MutableStateFlow<List<Memory>>(emptyList())
    val recentMemories: StateFlow<List<Memory>> = _recentMemories.asStateFlow()

    private val _memoryCount = MutableStateFlow(0)
    val memoryCount: StateFlow<Int> = _memoryCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadHomeData()
    }

    /**
     * Load recent memories and count from repository.
     */
    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Load recent memories (last 10)
                val recent = repository.getRecentMemories(limit = 10)
                _recentMemories.value = recent

                // Load total count
                val count = repository.getMemoryCount()
                _memoryCount.value = count

            } catch (e: Exception) {
                _error.value = "Failed to load memories: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Delete a memory.
     *
     * @param memory The memory to delete
     */
    fun deleteMemory(memory: Memory) {
        viewModelScope.launch {
            try {
                repository.deleteMemory(memory)

                // Refresh data
                loadHomeData()

            } catch (e: Exception) {
                _error.value = "Failed to delete memory: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    /**
     * Clear error message.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Refresh home data (pull to refresh).
     */
    fun refresh() {
        loadHomeData()
    }
}