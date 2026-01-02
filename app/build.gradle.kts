import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinComposeCompiler)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp) // ✅ ahora sí funciona y habilita ksp(...)
}

android {
    namespace = "com.example.pinkpanterwear"
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
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // Hilt
    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.playServicesAnalyticsImpl)
    ksp(libs.hiltCompiler)

    // Room
    implementation(libs.androidxRoomRuntime)
    implementation(libs.androidxRoomKtx)
    ksp(libs.androidxRoomCompiler)

    // Otros (Firebase, Compose, Coil, Retrofit, etc.)
    implementation(platform(libs.firebaseBomLib))
    implementation(libs.firebaseAnalyticsLib)
    implementation(libs.firebaseAuthKtxLib)
    implementation(libs.firebaseFirestoreKtxLib)
    implementation(libs.firebaseStorageKtxLib)
    implementation(libs.firebaseUiFirestoreLib)

    implementation(platform(libs.composeBomLib))
    implementation(libs.composeUi)
    implementation(libs.composeUiTooling)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeRuntime)
    implementation(libs.navigationCompose)

    implementation(libs.androidxCoreKtx)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidxMaterial)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)
    implementation(libs.androidxViewPager2)
    implementation(libs.androidxPagingRuntimeKtx)
    implementation(libs.androidxLifecycleViewModel)
    implementation(libs.androidxLifecycleRuntimeKtx) // Added for repeatOnLifecycle
    implementation(libs.fragmentKtx)

    implementation(libs.coilLib)
    implementation(libs.lottieLib)
    implementation(libs.picassoLib)
    implementation(libs.paperdbLib)
    implementation(libs.glide)
    ksp(libs.compiler)

    implementation(libs.retrofit)
    implementation(libs.converterGson)

    testImplementation(libs.junitLib)
    androidTestImplementation(libs.androidxJunitLib)
    androidTestImplementation(libs.espressoCoreLib)

    testImplementation(libs.mockkLib)
}
