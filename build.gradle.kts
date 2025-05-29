plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
