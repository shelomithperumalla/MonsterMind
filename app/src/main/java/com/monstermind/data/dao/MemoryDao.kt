package com.monstermind.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.monstermind.data.entity.Memory
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) for memory database operations.
 *
 * Provides methods to:
 * - Insert new memories
 * - Query memories by various criteria
 * - Delete memories
 * - Get reactive streams of data (Flow)
 *
 * All database operations are suspend functions (run on IO thread via Coroutines).
 */
@Dao
interface MemoryDao {

    /**
     * Insert a new memory into the database.
     * @return The row ID of the newly inserted memory
     */
    @Insert
    suspend fun insertMemory(memory: Memory): Long

    /**
     * Delete a memory from the database.
     */
    @Delete
    suspend fun deleteMemory(memory: Memory)

    /**
     * Get a single memory by ID.
     * Returns null if not found.
     */
    @Query("SELECT * FROM memories WHERE id = :id")
    suspend fun getMemoryById(id: Long): Memory?

    /**
     * Get all memories ordered by creation date (newest first).
     * Returns as Flow for reactive updates.
     */
    @Query("SELECT * FROM memories ORDER BY createdAt DESC")
    fun getAllMemoriesFlow(): Flow<List<Memory>>

    /**
     * Get all memories as a one-shot query.
     * (Use getAllMemoriesFlow for reactive updates)
     */
    @Query("SELECT * FROM memories ORDER BY createdAt DESC")
    suspend fun getAllMemories(): List<Memory>

    /**
     * Get memories by category.
     * @param category The category to filter by (e.g., "Travel", "Shopping")
     */
    @Query("SELECT * FROM memories WHERE category = :category ORDER BY createdAt DESC")
    suspend fun getMemoriesByCategory(category: String): List<Memory>

    /**
     * Search memories by text content (case-insensitive).
     * Uses LIKE with wildcards to match partial text.
     * @param searchText Text to search for (will be wrapped with % for wildcards)
     */
    @Query("""
        SELECT * FROM memories 
        WHERE extractedText LIKE '%' || :searchText || '%' 
        ORDER BY createdAt DESC
    """)
    suspend fun searchMemoriesByText(searchText: String): List<Memory>

    /**
     * Get recent memories (limited by count).
     * Useful for showing recent memories on home screen.
     */
    @Query("SELECT * FROM memories ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentMemories(limit: Int = 10): List<Memory>

    /**
     * Get count of all memories in database.
     * Useful for analytics and UI state.
     */
    @Query("SELECT COUNT(*) FROM memories")
    suspend fun getMemoryCount(): Int

    /**
     * Delete all memories (useful for dev/testing).
     * NOT exposed in production UI.
     */
    @Query("DELETE FROM memories")
    suspend fun deleteAllMemories()
}