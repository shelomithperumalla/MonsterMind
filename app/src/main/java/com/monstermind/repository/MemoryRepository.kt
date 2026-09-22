package com.monstermind.repository

import com.monstermind.classification.CategoryClassifier
import com.monstermind.data.dao.MemoryDao
import com.monstermind.data.entity.Memory
import com.monstermind.data.entity.MemorySearchResult
import com.monstermind.search.MemorySearchEngine
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Memory Repository
 *
 * Abstracts the data layer from business logic.
 *
 * Responsibilities:
 * 1. Manage memory persistence (database operations)
 * 2. Handle memory classification (categorization)
 * 3. Provide search functionality
 * 4. Convert database entities to UI models
 * 5. Handle errors gracefully
 *
 * Design Pattern: Repository Pattern
 * - Decouples data sources from business logic
 * - Makes testing easier (can mock repository)
 * - Makes it easy to add new data sources (cloud sync, local cache, etc.)
 *
 * Example:
 * val memory = Memory(
 *   imagePath = "/path/to/image.jpg",
 *   extractedText = "SUNBURN ARENA",
 *   category = "Events"
 * )
 * repository.addMemory(memory)
 * val results = repository.searchMemories("concert")
 */
@ActivityRetainedScoped
class MemoryRepository @Inject constructor(
    private val memoryDao: MemoryDao,
    private val classifier: CategoryClassifier,
    private val searchEngine: MemorySearchEngine
) {

    /**
     * Add a new memory to the database.
     *
     * @param imagePath Local file path to the image
     * @param extractedText OCR-extracted text from the image
     * @param source Optional source/app name (defaults to "Unknown source")
     * @return The ID of the newly inserted memory
     */
    suspend fun addMemory(
        imagePath: String,
        extractedText: String,
        source: String = "Unknown source"
    ): Long {
        // Classify the memory based on extracted text
        val category = classifier.classify(extractedText)

        // Create memory entity
        val memory = Memory(
            imagePath = imagePath,
            extractedText = extractedText,
            category = category,
            source = source,
            createdAt = System.currentTimeMillis()
        )

        // Insert into database
        return memoryDao.insertMemory(memory)
    }

    /**
     * Get a memory by ID.
     *
     * @param id The memory ID
     * @return The memory, or null if not found
     */
    suspend fun getMemoryById(id: Long): Memory? {
        return memoryDao.getMemoryById(id)
    }

    /**
     * Get all memories as a reactive Flow.
     *
     * Updates automatically when database changes.
     * Useful for observing memory list in UI.
     *
     * @return Flow of list of memories (newest first)
     */
    fun getAllMemoriesFlow(): Flow<List<Memory>> {
        return memoryDao.getAllMemoriesFlow()
    }

    /**
     * Get all memories (one-shot query).
     *
     * Use getAllMemoriesFlow() for reactive updates.
     *
     * @return List of all memories (newest first)
     */
    suspend fun getAllMemories(): List<Memory> {
        return memoryDao.getAllMemories()
    }

    /**
     * Get recent memories (last N).
     *
     * @param limit Maximum number of memories to return (default: 10)
     * @return List of recent memories
     */
    suspend fun getRecentMemories(limit: Int = 10): List<Memory> {
        return memoryDao.getRecentMemories(limit)
    }

    /**
     * Get memories by category.
     *
     * @param category The category to filter by
     * @return List of memories in that category (newest first)
     */
    suspend fun getMemoriesByCategory(category: String): List<Memory> {
        return memoryDao.getMemoriesByCategory(category)
    }

    /**
     * Search memories using natural-language query.
     *
     * Delegates to MemorySearchEngine for intelligent search.
     * Results are ranked by relevance.
     *
     * @param query The search query
     * @return List of matching memories ranked by relevance
     */
    suspend fun searchMemories(query: String): List<MemorySearchResult> {
        // Get all memories
        val allMemories = memoryDao.getAllMemories()

        // Delegate to search engine
        return searchEngine.search(query, allMemories)
    }

    /**
     * Simple text search (without relevance ranking).
     *
     * @param keyword Keyword to search for
     * @return Memories containing the keyword
     */
    suspend fun simpleSearch(keyword: String): List<Memory> {
        val allMemories = memoryDao.getAllMemories()
        return searchEngine.simpleSearch(keyword, allMemories)
    }

    /**
     * Delete a memory.
     *
     * @param memory The memory to delete
     */
    suspend fun deleteMemory(memory: Memory) {
        memoryDao.deleteMemory(memory)
    }

    /**
     * Get total count of memories in database.
     *
     * Useful for empty state handling and analytics.
     *
     * @return Number of memories
     */
    suspend fun getMemoryCount(): Int {
        return memoryDao.getMemoryCount()
    }

    /**
     * Classify text into a category (for preview/testing).
     *
     * @param text The text to classify
     * @return The predicted category
     */
    fun classifyText(text: String): String {
        return classifier.classify(text)
    }

    /**
     * Classify text with confidence score.
     *
     * @param text The text to classify
     * @return Pair of (category, confidence_0_to_1)
     */
    fun classifyTextWithConfidence(text: String): Pair<String, Float> {
        return classifier.classifyWithConfidence(text)
    }

    // Development/Testing methods (marked as internal for production)

    /**
     * Delete all memories (for testing/demo reset).
     * NOT exposed in production UI.
     */
    suspend fun deleteAllMemories() {
        memoryDao.deleteAllMemories()
    }
}