package com.monstermind.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.monstermind.data.dao.MemoryDao
import com.monstermind.data.entity.Memory

/**
 * Room Database for MonsterMind.
 *
 * Manages the SQLite database for storing memories locally on the device.
 *
 * Database Properties:
 * - Name: "monstermind_db.db"
 * - Version: 1 (increment if schema changes)
 * - Location: /data/data/com.monstermind/databases/
 * - Encryption: Device-level (via Android system encryption)
 *
 * Tables:
 * - memories: Stores all saved memories
 *
 * This is a singleton instance. Use getInstance() to get the database.
 */
@Database(
    entities = [Memory::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MemoryDatabase : RoomDatabase() {

    /**
     * Get the MemoryDao for database operations.
     */
    abstract fun memoryDao(): MemoryDao

    companion object {
        @Volatile
        private var Instance: MemoryDatabase? = null

        /**
         * Get or create the MemoryDatabase instance (singleton pattern).
         *
         * Thread-safe using double-checked locking.
         *
         * @param context Application context
         * @return The database instance
         */
        fun getInstance(context: Context): MemoryDatabase {
            return Instance ?: synchronized(this) {
                Instance ?: buildDatabase(context).also { Instance = it }
            }
        }

        /**
         * Build the database with Room.
         *
         * Configuration:
         * - Allows main thread queries (dev only, use suspendFunctions in production)
         * - Uses WAL (Write-Ahead Logging) for better concurrency
         * - Creates database in app-specific storage (private, encrypted)
         *
         * @param context Application context
         * @return A new MemoryDatabase instance
         */
        private fun buildDatabase(context: Context): MemoryDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MemoryDatabase::class.java,
                "monstermind_db.db"
            )
                // For development: allows queries on main thread (not recommended for production)
                .allowMainThreadQueries()
                .build()
        }
    }
}