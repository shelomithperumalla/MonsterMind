package com.monstermind.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.monstermind.ui.viewmodels.SaveMemoryViewModel

/**
 * Save Memory Screen - Image selection and analysis flow.
 *
 * Process:
 * 1. User selects image from gallery
 * 2. App shows preview
 * 3. User taps "Analyze Memory"
 * 4. Progress shown:
 *    - ✓ Image captured
 *    - ✓ Text detected (OCR)
 *    - ✓ Category identified
 *    - ✓ Memory saved
 * 5. User can return to home or save another
 *
 * Error Handling:
 * - Shows friendly message if no image selected
 * - Continues even if no text found
 * - Handles large images gracefully
 *
 * Example:
 * val viewModel: SaveMemoryViewModel = hiltViewModel()
 * SaveMemoryScreen(viewModel, onNavigateBack, onMemorySaved)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveMemoryScreen(
    viewModel: SaveMemoryViewModel,
    onNavigateBack: () -> Unit,
    onMemorySaved: () -> Unit
) {
    // Collect UI state
    val selectedImage = viewModel.selectedImage.collectAsState()
    val extractedText = viewModel.extractedText.collectAsState()
    val detectedCategory = viewModel.detectedCategory.collectAsState()
    val isAnalyzing = viewModel.isAnalyzing.collectAsState()
    val error = viewModel.error.collectAsState()

    val imageCaptured = viewModel.imageCaptured.collectAsState()
    val textDetected = viewModel.textDetected.collectAsState()
    val categorized = viewModel.categorized.collectAsState()
    val memorySaved = viewModel.memorySaved.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Save Memory") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Preview Section
            if (selectedImage.value != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Image(
                        bitmap = selectedImage.value!!.asImageBitmap(),
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Analyze Button
                if (!isAnalyzing.value && !memorySaved.value) {
                    Button(
                        onClick = { viewModel.analyzeImage() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Analyze Memory")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // No Image Selected State
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "📸",
                                style = MaterialTheme.typography.displaySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Select an image to analyze",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Progress Section
            if (isAnalyzing.value || (imageCaptured.value && textDetected.value)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Analyzing memory...",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress Items
                        ProgressItem(
                            label = "Image captured",
                            isComplete = imageCaptured.value,
                            isLoading = isAnalyzing.value && !textDetected.value
                        )

                        ProgressItem(
                            label = "Text detected",
                            isComplete = textDetected.value,
                            isLoading = isAnalyzing.value && textDetected.value && !categorized.value
                        )

                        ProgressItem(
                            label = "Category identified",
                            isComplete = categorized.value,
                            isLoading = isAnalyzing.value && categorized.value && !memorySaved.value
                        )

                        ProgressItem(
                            label = "Memory saved",
                            isComplete = memorySaved.value,
                            isLoading = false
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Error Message
            if (error.value != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error.value ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Results Section (after analysis)
            if (textDetected.value) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Extracted Information",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category
                        if (categorized.value) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Category",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Text(
                                        detectedCategory.value,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(8.dp, 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Extracted Text
                        if (extractedText.value.isNotEmpty()) {
                            Text(
                                "Text Detected:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                extractedText.value.take(200) + if (extractedText.value.length > 200) "..." else "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else {
                            Text(
                                "No text detected in image",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Success and Done Button
            if (memorySaved.value) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            modifier = Modifier
                                .size(48.dp)
                                .padding(bottom = 8.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            "Memory saved successfully!",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.reset()
                        onMemorySaved()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Return to Home")
                }
            }
        }
    }
}

/**
 * Progress indicator item for the analysis flow.
 */
@Composable
private fun ProgressItem(
    label: String,
    isComplete: Boolean,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        } else if (isComplete) {
            Text("✓", style = MaterialTheme.typography.titleMedium)
        } else {
            Text("○", style = MaterialTheme.typography.titleMedium)
        }

        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}