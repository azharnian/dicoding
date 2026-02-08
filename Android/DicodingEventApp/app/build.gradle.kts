plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.dicodingeventapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.dicodingeventapp"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
            jvmTarget.set(
                org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
            )
        }
    }

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        implementation("com.google.android.material:material:1.13.0")

        implementation("com.squareup.retrofit2:retrofit:3.0.0")
        implementation("com.squareup.retrofit2:converter-gson:3.0.0")

        implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

        implementation("androidx.recyclerview:recyclerview:1.4.0")

        implementation("com.github.bumptech.glide:glide:5.0.5")

        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")

        implementation("androidx.fragment:fragment-ktx:1.8.9")

    }
}