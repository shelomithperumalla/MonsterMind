package com.monstermind.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monstermind.ocr.TextRecognitionService
import com.monstermind.repository.MemoryRepository
import com.monstermind.util.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for Save Memory Screen.
 *
 * Manages the save/capture flow:
 * 1. User selects image
 * 2. App shows preview
 * 3. User taps "Analyze"
 * 4. OCR extracts text
 * 5. Category is assigned
 * 6. Memory is saved
 *
 * Uses:
 * - TextRecognitionService for OCR
 * - ImageUtils for image handling
 * - Repository for saving
 * - StateFlow for reactive UI updates
 *
 * Progress tracking:
 * - imageCaptured: Image selected/captured
 * - textDetected: OCR completed
 * - categorized: Category determined
 * - saved: Memory saved to database
 */
@HiltViewModel
class SaveMemoryViewModel @Inject constructor(
    private val repository: MemoryRepository,
    private val textRecognitionService: TextRecognitionService
) : ViewModel() {

    // UI State for save flow
    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage.asStateFlow()

    private val _extractedText = MutableStateFlow("")
    val extractedText: StateFlow<String> = _extractedText.asStateFlow()

    private val _detectedCategory = MutableStateFlow("")
    val detectedCategory: StateFlow<String> = _detectedCategory.asStateFlow()

    // Progress tracking
    private val _imageCaptured = MutableStateFlow(false)
    val imageCaptured: StateFlow<Boolean> = _imageCaptured.asStateFlow()

    private val _textDetected = MutableStateFlow(false)
    val textDetected: StateFlow<Boolean> = _textDetected.asStateFlow()

    private val _categorized = MutableStateFlow(false)
    val categorized: StateFlow<Boolean> = _categorized.asStateFlow()

    private val _memorySaved = MutableStateFlow(false)
    val memorySaved: StateFlow<Boolean> = _memorySaved.asStateFlow()

    // Loading and error states
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Set the selected image and mark as captured.
     *
     * @param bitmap The selected image
     */
    fun setSelectedImage(bitmap: Bitmap) {
        _selectedImage.value = bitmap
        _imageCaptured.value = true

        // Reset other states for new image
        _extractedText.value = ""
        _detectedCategory.value = ""
        _textDetected.value = false
        _categorized.value = false
        _memorySaved.value = false
        _error.value = null
    }

    /**
     * Analyze the selected image.
     *
     * Process:
     * 1. Extract text using ML Kit OCR
     * 2. Classify extracted text into category
     * 3. Update UI state
     * 4. Store memory in database
     *
     * If image is not selected, shows error.
     */
    fun analyzeImage() {
        val image = _selectedImage.value
        if (image == null) {
            _error.value = "No image selected"
            return
        }

        viewModelScope.launch {
            try {
                _isAnalyzing.value = true
                _error.value = null

                // Step 1: Image captured (already done)
                // Simulate delay for UX feedback
                kotlinx.coroutines.delay(500)

                // Step 2: Extract text using OCR
                _textDetected.value = false
                val text = textRecognitionService.recognizeText(image)
                _extractedText.value = text
                _textDetected.value = true

                if (text.isEmpty()) {
                    // No text found, but continue (user can still save image)
                    _error.value = "No readable text found. You can still save this image."
                }

                // Simulate delay for UX feedback
                kotlinx.coroutines.delay(300)

                // Step 3: Categorize
                _categorized.value = false
                val category = repository.classifyText(text)
                _detectedCategory.value = category
                _categorized.value = true

                // Simulate delay for UX feedback
                kotlinx.coroutines.delay(300)

                // Step 4: Save to database
                _memorySaved.value = false
                val imagePath = ImageUtils.saveImage(image)
                repository.addMemory(
                    imagePath = imagePath,
                    extractedText = text,
                    source = "Unknown source"
                )
                _memorySaved.value = true

            } catch (e: Exception) {
                _error.value = "Error analyzing image: ${e.message}"
                e.printStackTrace()
                // Reset progress on error
                _textDetected.value = false
                _categorized.value = false
                _memorySaved.value = false
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    /**
     * Reset all state for new save.
     */
    fun reset() {
        _selectedImage.value = null
        _extractedText.value = ""
        _detectedCategory.value = ""
        _imageCaptured.value = false
        _textDetected.value = false
        _categorized.value = false
        _memorySaved.value = false
        _isAnalyzing.value = false
        _error.value = null
    }

    /**
     * Clear error message.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Check if save flow is complete.
     */
    val isComplete: Boolean
        get() = imageCaptured.value && textDetected.value && categorized.value && memorySaved.value
}