// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.8" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}