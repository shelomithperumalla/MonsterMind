package com.monstermind.data.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Room Type Converters for handling non-primitive types in the database.
 *
 * Room only natively supports primitive types (Int, String, Long, etc.).
 * This class provides converters for more complex types like LocalDateTime.
 *
 * For this MVP, we primarily use Long (milliseconds since epoch) for dates,
 * but these converters are provided for future enhancements.
 */
class Converters {

    /**
     * Convert Long (milliseconds since epoch) to LocalDateTime.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(it),
                ZoneId.systemDefault()
            )
        }
    }

    /**
     * Convert LocalDateTime to Long (milliseconds since epoch).
     */
    @TypeConverter
    fun dateToTimestamp(value: LocalDateTime?): Long? {
        return value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
}