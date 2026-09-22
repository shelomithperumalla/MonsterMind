package com.monstermind.classification

import com.monstermind.data.entity.MemoryCategory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Category Classifier for memories.
 *
 * Classifies memory content into predefined categories using keyword matching.
 *
 * Strategy:
 * 1. Tokenize the input text into words
 * 2. Count keyword matches for each category
 * 3. Assign score based on match count
 * 4. Return category with highest score
 * 5. If tie or no matches, return "Other"
 *
 * Advantages:
 * - Deterministic (same input = same output)
 * - Fast (no ML model to load)
 * - Easy to debug and improve
 *
 * Future (Green Light):
 * - Can be replaced with LLM-based classification
 * - Can use embeddings for semantic understanding
 * - Can add custom user categories
 *
 * Example:
 * classifier.classify("Flight LH456 to Berlin")
 * // Returns: "Travel"
 *
 * classifier.classify("SUNBURN ARENA, BENGALURU, 25 OCTOBER")
 * // Returns: "Events"
 */
@Singleton
class CategoryClassifier @Inject constructor() {

    /**
     * Classify text into a memory category.
     *
     * @param text The extracted text to classify
     * @return The determined category name
     */
    fun classify(text: String): String {
        if (text.isBlank()) {
            return MemoryCategory.OTHER
        }

        // Normalize text: lowercase, trim whitespace
        val normalizedText = text.lowercase().trim()

        // Tokenize into words
        val words = tokenize(normalizedText)

        if (words.isEmpty()) {
            return MemoryCategory.OTHER
        }

        // Calculate scores for each category
        val scores = mutableMapOf<String, Int>()

        for (category in MemoryCategory.all) {
            val keywords = CategoryKeywords.getKeywordsForCategory(category)
            val matchCount = countMatches(words, keywords)
            if (matchCount > 0) {
                scores[category] = matchCount
            }
        }

        // Return category with highest score
        return scores.maxByOrNull { it.value }?.key ?: MemoryCategory.OTHER
    }

    /**
     * Classify with detailed scoring for debugging.
     *
     * @param text Text to classify
     * @return Pair of (category, confidence_0_to_1)
     */
    fun classifyWithConfidence(text: String): Pair<String, Float> {
        if (text.isBlank()) {
            return MemoryCategory.OTHER to 0f
        }

        val normalizedText = text.lowercase().trim()
        val words = tokenize(normalizedText)

        if (words.isEmpty()) {
            return MemoryCategory.OTHER to 0f
        }

        val scores = mutableMapOf<String, Int>()
        var maxScore = 0

        for (category in MemoryCategory.all) {
            val keywords = CategoryKeywords.getKeywordsForCategory(category)
            val matchCount = countMatches(words, keywords)
            if (matchCount > 0) {
                scores[category] = matchCount
                maxScore = maxOf(maxScore, matchCount)
            }
        }

        if (maxScore == 0) {
            return MemoryCategory.OTHER to 0f
        }

        // Find category with max score
        val topCategory = scores.maxByOrNull { it.value }?.key ?: MemoryCategory.OTHER

        // Calculate confidence as proportion of total words
        val confidence = (maxScore.toFloat() / words.size).coerceIn(0f, 1f)

        return topCategory to confidence
    }

    /**
     * Tokenize text into individual words.
     *
     * Process:
     * - Split by whitespace and punctuation
     * - Remove empty strings
     * - Keep only alphanumeric words (allow underscores, hyphens)
     *
     * @param text The text to tokenize
     * @return List of word tokens
     */
    private fun tokenize(text: String): List<String> {
        return text
            .split(Regex("[\\s\\p{P}]+"))  // Split by whitespace and punctuation
            .filter { it.isNotEmpty() }    // Remove empty tokens
            .map { it.lowercase() }         // Normalize to lowercase
    }

    /**
     * Count how many words match keywords in the set.
     *
     * Matching strategy:
     * - Exact word match (after tokenization)
     * - Partial match (word contains keyword or keyword contains word)
     *
     * @param words List of tokenized words
     * @param keywords Set of keywords to match against
     * @return Count of matches
     */
    private fun countMatches(words: List<String>, keywords: Set<String>): Int {
        var count = 0

        for (word in words) {
            for (keyword in keywords) {
                // Exact match
                if (word == keyword) {
                    count++
                    break  // Only count once per word
                }
                // Partial match (word contains keyword as substring)
                else if (word.contains(keyword) || keyword.contains(word)) {
                    // Only count longer substrings (avoid single letter matches)
                    if (keyword.length > 2 || word.length > 3) {
                        count++
                        break
                    }
                }
            }
        }

        return count
    }
}