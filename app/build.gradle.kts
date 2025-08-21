import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinComposeCompiler) // Or directly if not using project-level apply false

}

android {
    namespace = "com.example.pink"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.pinkpanterwear"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xskip-prerelease-check")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    kapt {
        correctErrorTypes = true
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // Core Android
    implementation(libs.androidxCoreKtx)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidxMaterial)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)
    implementation(libs.androidxViewPager2)
    implementation(libs.androidxPagingRuntimeKtx)
    implementation(libs.androidxLifecycleViewModel)
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Firebase
    implementation(platform(libs.firebaseBomLib))
    implementation(libs.firebaseAnalyticsLib)
    implementation(libs.firebaseAuthKtxLib)
    implementation(libs.firebaseFirestoreKtxLib)
    implementation(libs.firebaseStorageKtxLib)
    implementation(libs.firebaseUiFirestoreLib)

    // Jetpack Compose
    implementation(platform(libs.composeBomLib))
    implementation(libs.composeUi)
    implementation(libs.composeUiTooling)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeRuntime)
    implementation(libs.navigationCompose)

    // Hilt
    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavigationCompose)
    kapt(libs.hiltCompiler)

    // Lottie
    implementation(libs.lottieLib)

    // Glide
    implementation(libs.glideLib)
    kapt(libs.glideCompiler)

    // Picasso
    implementation(libs.picassoLib)

    // PaperDB
    implementation(libs.paperdbLib)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Room
    implementation(libs.androidxRoomRuntime)
    implementation(libs.androidxRoomKtx)
    kapt(libs.androidxRoomCompiler)

    // Google Analytics
    implementation(libs.playServicesAnalyticsLib)
    implementation(libs.playServicesAnalyticsCore)
    implementation(libs.playServicesAnalyticsV1811)


    // Testing
    testImplementation(libs.junitLib)
    androidTestImplementation(libs.androidxJunitLib)
    androidTestImplementation(libs.espressoCoreLib)
}