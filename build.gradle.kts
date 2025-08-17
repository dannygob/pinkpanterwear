plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.googleServices) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
