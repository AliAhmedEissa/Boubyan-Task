package com.boubyan.nytimesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class for the NY Times App.
 * This class is annotated with @HiltAndroidApp to enable Hilt dependency injection
 * throughout the application.
 */
@HiltAndroidApp
class NYTimesApplication : Application()