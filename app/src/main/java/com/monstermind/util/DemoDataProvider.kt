package com.monstermind.util

import com.monstermind.data.entity.Memory
import com.monstermind.data.entity.MemoryCategory

/**
 * Demo Data Provider for MonsterMind.
 *
 * Provides sample memories for testing and hackathon demos.
 * Allows quick app verification without taking real screenshots.
 *
 * Usage:
 * val demoMemories = DemoDataProvider.getDemoMemories()
 * for (memory in demoMemories) {
 *   repository.addMemory(memory)
 * }
 *
 * Demo memories include:
 * 1. Concert poster (Events)
 * 2. Laptop shopping (Shopping)
 * 3. DSA notes (Education)
 * 4. Valorant achievement (Gaming)
 */
object DemoDataProvider {

    /**
     * Get all demo memories.
     * Each memory includes:
     * - Realistic extracted text
     * - Appropriate category
     * - Simulated timestamp (spread across recent days)
     *
     * Note: In real implementation, imagePath would point to actual files.
     * For demo, use placeholder paths.
     */
    fun getDemoMemories(): List<Memory> {
        val baseTime = System.currentTimeMillis()

        return listOf(
            // Memory 1: Concert Poster
            Memory(
                id = 1,
                imagePath = "/data/data/com.monstermind/files/demo_concert.jpg",
                extractedText = """
                    SUNBURN ARENA
                    BENGALURU
                    25 OCTOBER 2026
                    
                    FEATURING:
                    • INDIE BAND XYZ
                    • ELECTRONIC PRODUCER ABC
                    • LIVE DJ SET
                    
                    BOOK NOW
                    www.example.com/tickets
                    PRICE: ₹1500 - ₹5000
                """.trimIndent(),
                category = MemoryCategory.EVENTS,
                source = "Unknown source",
                createdAt = baseTime - (2 * 24 * 60 * 60 * 1000) // 2 days ago
            ),

            // Memory 2: Laptop Shopping
            Memory(
                id = 2,
                imagePath = "/data/data/com.monstermind/files/demo_laptop.jpg",
                extractedText = """
                    LAPTOP OFFER
                    
                    Dell XPS 13
                    Intel Core i7
                    16GB RAM
                    512GB SSD
                    OLED Display
                    
                    ORIGINAL PRICE: ₹89,999
                    OFFER PRICE: ₹69,999
                    DISCOUNT: 22% OFF
                    
                    AVAILABLE ON
                    Amazon | Flipkart
                    
                    FREE SHIPPING
                    30-DAY RETURN POLICY
                """.trimIndent(),
                category = MemoryCategory.SHOPPING,
                source = "Unknown source",
                createdAt = baseTime - (1 * 24 * 60 * 60 * 1000) // 1 day ago
            ),

            // Memory 3: DSA Notes
            Memory(
                id = 3,
                imagePath = "/data/data/com.monstermind/files/demo_notes.jpg",
                extractedText = """
                    DATA STRUCTURES & ALGORITHMS
                    
                    BINARY SEARCH TREE (BST)
                    - Left child < Parent < Right child
                    - Time Complexity: O(log n) average
                    - Insertion: Find correct position, add node
                    - Deletion: 3 cases (leaf, one child, two children)
                    
                    BALANCED BST (AVL)
                    - Height difference ≤ 1
                    - Self-balancing on insertion/deletion
                    - Rotation operations: LL, RR, LR, RL
                    
                    ASSIGNMENT DUE: 30 September
                    EXAM COVERAGE: Chapters 5-7
                """.trimIndent(),
                category = MemoryCategory.EDUCATION,
                source = "Unknown source",
                createdAt = baseTime - (3 * 24 * 60 * 60 * 1000) // 3 days ago
            ),

            // Memory 4: Gaming Achievement
            Memory(
                id = 4,
                imagePath = "/data/data/com.monstermind/files/demo_gaming.jpg",
                extractedText = """
                    VALORANT ACE
                    
                    ROUND 23 - HAVEN
                    
                    KILLS: 5
                    DEATHS: 0
                    DAMAGE: 187
                    
                    WEAPONS USED
                    • Operator - 2 kills
                    • Phantom - 2 kills
                    • Knife - 1 kill
                    
                    TEAM: RADIANT
                    OPPONENT: IMMORTAL
                    RESULT: VICTORY
                    
                    ACE WITHOUT ABILITIES!
                """.trimIndent(),
                category = MemoryCategory.GAMING,
                source = "Unknown source",
                createdAt = baseTime // Just now
            )
        )
    }

    /**
     * Get a single demo memory by index.
     */
    fun getDemoMemory(index: Int): Memory? {
        return getDemoMemories().getOrNull(index)
    }

    /**
     * Get demo memories by category.
     */
    fun getDemoMemoriesByCategory(category: String): List<Memory> {
        return getDemoMemories().filter { it.category == category }
    }
}