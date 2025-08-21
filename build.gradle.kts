plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlinComposeCompiler) apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
