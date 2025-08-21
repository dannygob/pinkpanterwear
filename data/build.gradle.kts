plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kapt)
    // alias(libs.plugins.ksp) // Si decides migrar Room a KSP
}

android {
    namespace = "com.example.pinkpanterwear.data"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Módulo de dominio
    implementation(project(":domain"))

    // Hilt (inyección de dependencias)
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    // Retrofit + Gson (API REST)
    implementation(libs.retrofit)
    implementation(libs.gsonConverter)

    // Firebase Firestore
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseFirestore)

    // Room (persistencia local)
    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    kapt(libs.roomCompiler)
    // Si migras a KSP:
    // ksp(libs.roomCompiler)
}