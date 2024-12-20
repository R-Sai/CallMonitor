// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    id("com.android.library") version "8.7.3" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.21" apply false
}

buildscript {
    val agp_version by extra("8.7.3")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:2.0.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}