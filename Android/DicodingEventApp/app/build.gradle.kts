plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.github.ben-manes.versions") version "0.53.0"
    id("kotlin-parcelize")
    id("kotlin-kapt")
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
        implementation(libs.material)

        implementation(libs.retrofit)
        implementation(libs.retrofit.gson)

        implementation(libs.okhttp.logging)
        implementation(libs.recyclerview)

        implementation(libs.glide)
        implementation(libs.lifecycle.viewmodel)
        implementation(libs.lifecycle.livedata)
        implementation(libs.fragment.ktx)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation(libs.room.runtime)
        implementation(libs.room.ktx)
        kapt(libs.room.compiler)

        implementation(libs.androidx.datastore.preferences)
    }
}