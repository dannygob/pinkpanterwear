plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.pinkpanterwear.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    // Other dependencies will be added later
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseFirestoreKtx)
}
