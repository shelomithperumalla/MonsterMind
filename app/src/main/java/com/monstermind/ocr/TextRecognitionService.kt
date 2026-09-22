package com.monstermind.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Text Recognition Service using Google ML Kit.
 *
 * Provides on-device OCR (Optical Character Recognition) for extracting text from images.
 *
 * Features:
 * - On-device processing (no API calls, privacy-first)
 * - Supports multiple languages (configured for Latin scripts)
 * - Fast processing (typically 0.5-2 seconds per image)
 * - Graceful error handling
 *
 * Limitations:
 * - Limited to Latin characters (configurable)
 * - Performance depends on image quality
 * - Blurry/rotated images may have poor results
 *
 * Example:
 * val textRecognitionService = TextRecognitionService()
 * val extractedText = textRecognitionService.recognizeText(bitmap)
 * // Returns: "SUNBURN ARENA\nBENGALURU\n25 OCTOBER 2026\nBOOK NOW"
 */
@Singleton
class TextRecognitionService @Inject constructor() {

    /**
     * Recognize/extract text from a bitmap image.
     *
     * Process:
     * 1. Create InputImage from bitmap
     * 2. Initialize ML Kit text recognizer (Latin characters)
     * 3. Run recognition asynchronously
     * 4. Extract all detected text blocks
     * 5. Combine into single string with newlines
     *
     * @param bitmap The image bitmap to extract text from
     * @return Extracted text, or empty string if no text found or error occurs
     *
     * Thread: Must be called from coroutine context (see recognizeTextAsync)
     */
    suspend fun recognizeText(bitmap: Bitmap): String = withContext(Dispatchers.Default) {
        try {
            // Create InputImage from bitmap
            val inputImage = InputImage.fromBitmap(bitmap, 0)

            // Initialize ML Kit text recognizer for Latin characters
            val recognizer = TextRecognition.getClient(
                TextRecognizerOptions.Builder().build()
            )

            // Run text recognition (async within coroutine)
            val result = recognizer.process(inputImage).await()

            // Extract all text blocks and combine them
            val extractedText = result.text.ifEmpty { "" }

            return@withContext extractedText
        } catch (e: Exception) {
            // Gracefully handle errors
            // Log error but don't crash
            e.printStackTrace()
            return@withContext ""
        }
    }

    /**
     * Alternative: Recognize text with callback (for non-coroutine contexts).
     * Returns immediately, result comes via callback.
     *
     * @param bitmap Image to process
     * @param onSuccess Called with extracted text
     * @param onError Called if recognition fails
     */
    fun recognizeTextAsync(
        bitmap: Bitmap,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(
                TextRecognizerOptions.Builder().build()
            )

            recognizer.process(inputImage)
                .addOnSuccessListener { result ->
                    onSuccess(result.text)
                }
                .addOnFailureListener { exception ->
                    onError(exception)
                }
        } catch (e: Exception) {
            onError(e)
        }
    }
}