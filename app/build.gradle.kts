// app/build.gradle.kts

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.googleServices) // Firebase
}

android {
    namespace = "com.example.pinkpanterwear"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.pinkpanterwear"
        minSdk = 24
        targetSdk = libs.versions.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":presentation"))

    // Core + UI
    implementation(libs.androidxCoreKtx)
    implementation(libs.androidxAppcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.androidxFragmentKtx)
    implementation(libs.androidxActivityKtx)
    implementation(libs.androidxLifecycleLivedataKtx)
    implementation(libs.androidxLifecycleViewmodelKtx)
    implementation(libs.androidxLifecycleRuntimeKtx)

    // Coroutines
    implementation(libs.kotlinxCoroutinesCore)
    implementation(libs.kotlinxCoroutinesAndroid)

    // Jetpack Compose
    implementation(libs.androidxActivityCompose)
    implementation(platform(libs.androidxComposeBom))
    implementation(libs.androidxUi)
    implementation(libs.androidxUiGraphics)
    implementation(libs.androidxUiToolingPreview)
    implementation(libs.androidxMaterial3)
    implementation(libs.firebaseFirestoreKtx)
    debugImplementation(libs.androidxUiTooling)
    debugImplementation(libs.androidxUiTestManifest)

    // Retrofit + Gson + Glide
    implementation(libs.retrofit)
    implementation(libs.gsonConverter)
    implementation(libs.glide)

    // Room (KSP)
    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)

    // Firebase
    implementation(libs.firebaseAuthKtx)
    implementation(libs.firebaseCommonKtx)

    // Hilt
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    // Otros (ejemplo genérico con exclusión)
    implementation("com.example.library:some-library:1.0.0") {
        exclude(group = "com.conflicting.group", module = "conflicting-module")
    }

    // Extra libs (placeholder)
    implementation(libs.library)
    implementation(libs.anotherLibrary)
    implementation(libs.transitiveLib)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidxJunit)
    androidTestImplementation(libs.androidxEspressoCore)
    androidTestImplementation(platform(libs.androidxComposeBom))
    androidTestImplementation(libs.androidxUiTestJunit4)

    implementation(platform(libs.firebaseBom))

}
