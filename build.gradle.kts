// build.gradle.kts (root)

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kapt) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
