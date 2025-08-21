plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.pinkpanterwear.domain"
    compileSdk = 33

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
    // Solo dependencias necesarias para lógica de negocio
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinxCoroutinesCore)

    // Testing
    testImplementation(libs.junit)
}