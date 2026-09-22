package com.monstermind

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class configured with Hilt for dependency injection.
 *
 * This enables:
 * - Automatic dependency injection throughout the app
 * - Repository injection in ViewModels
 * - Database injection in repositories
 * - Service injection where needed
 *
 * All @HiltViewModel and @HiltAndroidApp components will work automatically.
 */
@HiltAndroidApp
class MyApp : Application()