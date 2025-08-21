import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinComposeCompiler)
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
    implementation(libs.androidxCoreKtx)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidxMaterial)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)
    implementation(libs.androidxViewPager2)
    implementation(libs.androidxPagingRuntimeKtx)
    implementation(libs.androidxLifecycleViewModel)
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Firebase (BOM)
    implementation(platform(libs.firebaseBomLib))
    implementation(libs.firebaseAnalyticsLib)
    implementation(libs.firebaseAuthKtxLib)
    implementation(libs.firebaseFirestoreKtxLib)
    implementation(libs.firebaseStorageKtxLib)
    implementation(libs.firebaseUiFirestoreLib)

    implementation(libs.glideLib)


    // Hilt
    implementation(libs.hiltAndroid)
    implementation(libs.roomCommonJvm)
    implementation(libs.uiAndroid)
    kapt(libs.hiltCompiler)
}
