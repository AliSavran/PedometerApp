buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")    }
}

plugins {
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.4" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.0" apply false
}