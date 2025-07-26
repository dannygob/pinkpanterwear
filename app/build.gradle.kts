plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pink"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pink"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}


dependencies {
    // AndroidX base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase platform BOM
    implementation(platform(libs.firebase.bom.v3310))
    implementation(libs.google.firebase.firestore.ktx)
    implementation(libs.google.firebase.storage.ktx)
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.google.firebase.analytics)

    // Firebase UI with Paging3
    implementation(libs.firebase.ui.firestore.v802)
    implementation(libs.androidx.paging.runtime.ktx.v321)

    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
// Or the latest version



    implementation(libs.picasso)
    implementation("com.airbnb.android:lottie:5.2.0")

    // Local storage
    implementation("io.github.pilgr:paperdb:2.7.2")
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.play.services.analytics.impl)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.viewpager2)

}
