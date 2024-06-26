buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    kotlin("kapt") version "1.9.23"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id ("androidx.navigation.safeargs.kotlin") version "2.5.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.ncorti.ktfmt.gradle") version "0.18.0"
}