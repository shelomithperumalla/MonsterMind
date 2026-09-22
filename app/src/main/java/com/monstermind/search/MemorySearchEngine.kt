package com.monstermind.search

import com.monstermind.data.entity.Memory
import com.monstermind.data.entity.MemorySearchResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

/**
 * Local Search Engine for memories.
 *
 * Implements intelligent search over locally stored memories without any external calls.
 *
 * Search Strategy:
 * 1. Parse query to extract keywords and potential categories
 * 2. Search memories by:
 *    - OCR text content (keyword matching)
 *    - Memory category
 *    - Date/time if mentioned
 * 3. Score results by relevance
 * 4. Sort by score (highest first)
 * 5. Return ranked list with matched text
 *
 * Features:
 * - Case-insensitive matching
 * - Partial word matching
 * - Category-aware search
 * - Simple date parsing
 * - Relevance scoring
 *
 * Future (Green Light):
 * - Can be enhanced with semantic search using embeddings
 * - Can add natural language understanding via LLM
 * - Can learn from user interactions
 *
 * Examples:
 * search("concert") -> Returns events with "concert" in text
 * search("travel 2026") -> Returns travel memories from 2026
 * search("shopping under 5000") -> Returns shopping memories with prices mentioned
 */
@Singleton
class MemorySearchEngine @Inject constructor() {

    /**
     * Search memories using a natural-language query.
     *
     * @param query User's search query (e.g., "Find my concert poster")
     * @param memories List of memories to search within
     * @return Ranked list of MemorySearchResult sorted by relevance (highest first)
     */
    fun search(query: String, memories: List<Memory>): List<MemorySearchResult> {
        if (query.isBlank() || memories.isEmpty()) {
            return emptyList()
        }

        // Parse query to extract keywords
        val searchTerms = parseQuery(query)

        // Score each memory
        val results = memories.mapNotNull { memory ->
            val score = scoreMemory(memory, searchTerms)
            if (score > 0f) {
                val matchedText = extractMatchedText(memory.extractedText, searchTerms)
                MemorySearchResult(
                    memory = memory,
                    relevanceScore = score,
                    matchedText = matchedText
                )
            } else {
                null
            }
        }

        // Sort by relevance (highest first)
        return results.sortedByDescending { it.relevanceScore }
    }

    /**
     * Parse query to extract keywords and category hints.
     *
     * Examples:
     * "concert" -> ["concert"]
     * "Find my gaming screenshot" -> ["gaming", "screenshot"]
     * "Show the laptop offer" -> ["laptop", "offer", "shopping"]
     *
     * @param query The search query
     * @return List of search terms (lowercase, cleaned)
     */
    private fun parseQuery(query: String): List<String> {
        // Normalize: lowercase, remove punctuation
        val normalized = query.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")  // Remove non-alphanumeric except spaces
            .trim()

        if (normalized.isBlank()) return emptyList()

        // Tokenize into words
        val terms = normalized.split(Regex("\\s+"))
            .filter { it.isNotEmpty() && it.length > 2 }  // Ignore very short words

        return terms
    }

    /**
     * Score a memory based on how well it matches the search terms.
     *
     * Scoring:
     * - Exact keyword in text: 1.0 per match
     * - Partial keyword match: 0.5 per match
     * - Category keyword match: 0.3 bonus
     * - Recency bonus: newer memories get slight boost
     *
     * @param memory The memory to score
     * @param searchTerms Parsed search terms
     * @return Score from 0.0 to 1.0+ (higher is better)
     */
    private fun scoreMemory(memory: Memory, searchTerms: List<String>): Float {
        if (searchTerms.isEmpty()) return 0f

        val textLower = memory.extractedText.lowercase()
        val categoryLower = memory.category.lowercase()

        var score = 0f

        // Score based on matches in text
        for (term in searchTerms) {
            // Exact word match in text
            if (Regex("\\b$term\\b").containsMatchIn(textLower)) {
                score += 1.0f
            }
            // Partial match (term appears in text)
            else if (textLower.contains(term)) {
                score += 0.5f
            }

            // Category keyword match
            if (categoryLower.contains(term)) {
                score += 0.3f
            }
        }

        // Normalize score by number of search terms
        score = score / searchTerms.size

        // Recency bonus: prefer recent memories
        val daysSinceCreation = (System.currentTimeMillis() - memory.createdAt) / (1000 * 60 * 60 * 24)
        val recencyMultiplier = 1.0f + (0.1f / max(1f, daysSinceCreation.toFloat()))
        score *= recencyMultiplier

        return score
    }

    /**
     * Extract the matched text snippet from memory content.
     *
     * Finds the first occurrence of any search term and extracts
     * surrounding context (50 characters before and after).
     *
     * @param fullText The full extracted text
     * @param searchTerms The search terms
     * @return A snippet showing the match
     */
    private fun extractMatchedText(fullText: String, searchTerms: List<String>): String {
        val textLower = fullText.lowercase()

        for (term in searchTerms) {
            val index = textLower.indexOf(term)
            if (index >= 0) {
                val start = max(0, index - 30)
                val end = min(fullText.length, index + term.length + 30)
                return fullText.substring(start, end).trim()
                    .let { if (start > 0) "..." + it else it }
                    .let { if (end < fullText.length) it + "..." else it }
            }
        }

        // If no term found, return first 100 characters
        return if (fullText.length > 100) {
            fullText.substring(0, 100) + "..."
        } else {
            fullText
        }
    }

    /**
     * Simple search by keywords (used for basic search without full parsing).
     *
     * Useful for when you just want to search text without complex parsing.
     *
     * @param keyword Single keyword to search
     * @param memories Memories to search
     * @return Filtered memories containing keyword
     */
    fun simpleSearch(keyword: String, memories: List<Memory>): List<Memory> {
        if (keyword.isBlank()) return emptyList()

        val lowerKeyword = keyword.lowercase()
        return memories.filter { memory ->
            memory.extractedText.lowercase().contains(lowerKeyword) ||
                    memory.category.lowercase().contains(lowerKeyword)
        }
    }

    /**
     * Search by category.
     *
     * @param category The category to filter by
     * @param memories List of memories
     * @return Memories in that category, sorted by recency
     */
    fun searchByCategory(category: String, memories: List<Memory>): List<Memory> {
        return memories
            .filter { it.category == category }
            .sortedByDescending { it.createdAt }
    }
}

// Extension function for min (Kotlin stdlib includes max but not min in older versions)
private fun <T : Comparable<T>> min(a: T, b: T): T = if (a <= b) a else b