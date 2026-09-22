package com.monstermind

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.monstermind.data.entity.Memory
import com.monstermind.ui.screens.HomeScreen
import com.monstermind.ui.screens.MemoryDetailScreen
import com.monstermind.ui.screens.SaveMemoryScreen
import com.monstermind.ui.screens.SearchScreen
import com.monstermind.ui.theme.MonsterMindTheme
import com.monstermind.ui.viewmodels.HomeViewModel
import com.monstermind.ui.viewmodels.SaveMemoryViewModel
import com.monstermind.ui.viewmodels.SearchViewModel
import com.monstermind.util.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point for MonsterMind.
 *
 * Responsibilities:
 * 1. Initialize Compose UI
 * 2. Setup navigation between screens
 * 3. Handle permissions for image access
 * 4. Manage image selection from gallery
 * 5. Initialize utilities (ImageUtils, database)
 *
 * Navigation Structure:
 * - home: Home screen with recent memories
 * - saveMemory: Image selection and analysis
 * - search: Search interface
 * - memoryDetail/{id}: View single memory
 *
 * Example:
 * When app launches:
 * 1. onCreate() called
 * 2. Hilt injects dependencies
 * 3. Compose UI created with navigation
 * 4. User navigates between screens
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ImageUtils with app file directory
        ImageUtils.initialize(filesDir)

        // Set Compose content
        setContent {
            MonsterMindTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navigation setup
                    MonsterMindNavigation()
                }
            }
        }
    }
}

/**
 * Main navigation composable for MonsterMind.
 *
 * Manages all screen navigation and state.
 * Handles:
 * - Permission requests for gallery access
 * - Image selection from device gallery
 * - Screen transitions
 * - ViewModel injection
 */
@OptIn(ExperimentalPermissionsApi::class)
@androidx.compose.runtime.Composable
fun MonsterMindNavigation() {
    val navController = rememberNavController()

    // State for managing selected memory in detail screen
    var selectedMemory by mutableStateOf<Memory?>(null)

    // Permission state for reading images
    val readImagesPermission = rememberPermissionState(
        android.Manifest.permission.READ_MEDIA_IMAGES
    ) { isGranted ->
        if (!isGranted) {
            // Permission denied - show error in UI
            // User can go to settings to enable
        }
    }

    val context = LocalContext.current

    // Launcher for picking image from gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Get bitmap from URI and pass to SaveMemoryViewModel
            val bitmap = context.getBitmapFromUri(uri)
            if (bitmap != null) {
                // Navigate to SaveMemory screen with image
                navController.navigate("saveMemory") {
                    // The ViewModel will receive the image
                }
            }
        }
    }

    // Navigation host with all screens
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Home Screen
        composable("home") {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSave = {
                    // Request permission if needed
                    if (readImagesPermission.status.isGranted) {
                        imagePickerLauncher.launch("image/*")
                    } else {
                        readImagesPermission.launchPermissionRequest()
                    }
                },
                onNavigateToSearch = {
                    navController.navigate("search")
                },
                onNavigateToDetail = { memory ->
                    selectedMemory = memory
                    navController.navigate("memoryDetail/${memory.id}")
                }
            )
        }

        // Save Memory Screen
        composable("saveMemory") {
            val viewModel: SaveMemoryViewModel = hiltViewModel()

            // Launch image picker if no image selected
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                if (uri != null) {
                    val bitmap = context.getBitmapFromUri(uri)
                    if (bitmap != null) {
                        viewModel.setSelectedImage(bitmap)
                    }
                }
            }

            SaveMemoryScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onMemorySaved = {
                    // Return to home after successful save
                    navController.navigate("home") {
                        // Clear back stack to prevent returning to save screen
                        popUpTo("home") { inclusive = false }
                    }
                }
            )

            // Auto-launch picker if no image selected yet
            if (viewModel.selectedImage.value == null) {
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    if (readImagesPermission.status.isGranted) {
                        launcher.launch("image/*")
                    } else {
                        readImagesPermission.launchPermissionRequest()
                    }
                }
            }
        }

        // Search Screen
        composable("search") {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetail = { memory ->
                    selectedMemory = memory
                    navController.navigate("memoryDetail/${memory.id}")
                }
            )
        }

        // Memory Detail Screen
        composable(
            route = "memoryDetail/{memoryId}",
            arguments = listOf(navArgument("memoryId") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getLong("memoryId")
            val memory = selectedMemory

            if (memory != null && memory.id == memoryId) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                MemoryDetailScreen(
                    memory = memory,
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onDelete = { deletedMemory ->
                        homeViewModel.deleteMemory(deletedMemory)
                    }
                )
            }
        }
    }
}

/**
 * Helper function to get bitmap from URI (must be called from Context).
 */
private fun android.content.Context.getBitmapFromUri(uri: Uri): Bitmap? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        android.graphics.BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}