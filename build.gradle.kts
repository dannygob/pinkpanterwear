plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.ksp) apply false // ✅ reemplaza kapt
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory.get()) // ✅ moderno, sin deprecaciones
}