plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.googleServices)
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
        vectorDrawables {
            useSupportLibrary = true
        }
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

    kotlinOptions {
        jvmTarget = "17"
        languageVersion = "2.0" // Explicitly set Kotlin language version
        freeCompilerArgs += "-Xskip-prerelease-check" // To suppress the prerelease check warning
    }

    kapt {
        correctErrorTypes = true
        // Explicitly enable language version 2.0+ for Kapt
        arguments {
            arg("kotlin.compiler.languageVersion", "2.0")
        }
    }

    buildFeatures {
        compose = true
    }



    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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

    implementation(platform(libs.firebaseBomLib))
    implementation(libs.firebaseAnalyticsLib)
    implementation(libs.firebaseAuthKtxLib)
    implementation(libs.firebaseFirestoreKtxLib)
    implementation(libs.firebaseStorageKtxLib)
    implementation(libs.firebaseUiFirestoreLib)

    implementation(libs.glideLib)
    kapt(libs.glideCompiler)

    implementation(libs.androidxRoomRuntime)
    implementation(libs.androidxRoomKtx)
    kapt(libs.androidxRoomCompiler)
    implementation(libs.lifecycleViewmodel)

    implementation("androidx.fragment:fragment-ktx:1.3.0")


    implementation(libs.picassoLib)
    implementation(libs.lottieLib)
    implementation(libs.paperdbLib)

    implementation(libs.playServicesAnalyticsLib)

    testImplementation(libs.junitLib)
    androidTestImplementation(libs.androidxJunitLib)
    androidTestImplementation(libs.espressoCoreLib)
}
