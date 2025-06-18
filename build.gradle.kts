plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version libs.versions.hilt.get() apply false
}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}

//tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
//}
