package com.monstermind.util

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * Image Utility Functions
 *
 * Handles:
 * - Saving images to local app storage
 * - Compressing images to reduce size
 * - Generating unique filenames
 * - Cleanup of old images (if needed)
 *
 * Storage Location:
 * /data/data/com.monstermind/files/
 *
 * This directory is private to the app and encrypted by the system.
 */
object ImageUtils {

    // Private app files directory
    private var appFilesDir: File? = null

    /**
     * Initialize ImageUtils with app context.
     *
     * Must be called once during app startup.
     *
     * @param filesDir The app's files directory (context.filesDir)
     */
    fun initialize(filesDir: File) {
        appFilesDir = filesDir
    }

    /**
     * Save a bitmap image to local storage.
     *
     * Process:
     * 1. Compress bitmap to JPEG format
     * 2. Generate unique filename with timestamp
     * 3. Save to app-private storage
     * 4. Return file path
     *
     * @param bitmap The image bitmap to save
     * @param quality JPEG quality (0-100, default 85)
     * @return The saved image file path
     * @throws IllegalStateException if ImageUtils not initialized
     * @throws Exception if save fails
     */
    fun saveImage(bitmap: Bitmap, quality: Int = 85): String {
        val filesDir = appFilesDir ?: throw IllegalStateException(
            "ImageUtils not initialized. Call initialize(context.filesDir) first."
        )

        // Generate unique filename
        val filename = "memory_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, filename)

        try {
            // Compress and save
            FileOutputStream(file).use { outStream ->
                // Resize if too large (max 2048x2048)
                val resized = resizeIfNeeded(bitmap, maxSize = 2048)

                // Compress to JPEG
                resized.compress(Bitmap.CompressFormat.JPEG, quality, outStream)
                outStream.flush()
            }

            return file.absolutePath
        } catch (e: Exception) {
            // Delete file if save failed
            if (file.exists()) {
                file.delete()
            }
            throw e
        }
    }

    /**
     * Resize bitmap if it exceeds maximum dimensions.
     *
     * Maintains aspect ratio.
     *
     * @param bitmap Original bitmap
     * @param maxSize Maximum width or height in pixels
     * @return Resized bitmap, or original if already smaller
     */
    private fun resizeIfNeeded(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }

        // Calculate scale factor
        val scale = minOf(
            maxSize.toFloat() / width,
            maxSize.toFloat() / height
        )

        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * Get file size of image in MB.
     */
    fun getImageSizeInMB(filePath: String): Float {
        return try {
            val file = File(filePath)
            if (file.exists()) {
                file.length().toFloat() / (1024 * 1024)
            } else {
                0f
            }
        } catch (e: Exception) {
            0f
        }
    }

    /**
     * Delete an image file.
     *
     * @param filePath Path to the image file
     * @return True if deleted successfully
     */
    fun deleteImage(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get total storage used by all images in app.
     *
     * @return Total size in MB
     */
    fun getTotalStorageUsedMB(): Float {
        return try {
            val filesDir = appFilesDir ?: return 0f
            var totalSize = 0L

            filesDir.listFiles()?.forEach { file ->
                if (file.isFile && (file.name.endsWith(".jpg") || file.name.endsWith(".jpeg"))) {
                    totalSize += file.length()
                }
            }

            totalSize.toFloat() / (1024 * 1024)
        } catch (e: Exception) {
            0f
        }
    }

    /**
     * Cleanup old images (optional, for future use).
     *
     * Deletes images older than specified days.
     *
     * @param daysOld Delete images older than this many days
     * @return Number of images deleted
     */
    fun cleanupOldImages(daysOld: Int = 30): Int {
        return try {
            val filesDir = appFilesDir ?: return 0
            val cutoffTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L)
            var deleted = 0

            filesDir.listFiles()?.forEach { file ->
                if (file.isFile && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        deleted++
                    }
                }
            }

            deleted
        } catch (e: Exception) {
            0
        }
    }
}