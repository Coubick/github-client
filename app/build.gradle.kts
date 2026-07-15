import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

localProperties.load(FileInputStream(localPropertiesFile))

val githubClientIdDev = localProperties.getProperty("GITHUB_CLIENT_ID_DEV")
val githubClientSecretDev = localProperties.getProperty("GITHUB_CLIENT_SECRET_DEV")

val githubClientIdProd = localProperties.getProperty("GITHUB_CLIENT_ID_PROD")
val githubClientSecretProd = localProperties.getProperty("GITHUB_CLIENT_SECRET_PROD")


android {
    namespace = "ru.example.gitsource"
    compileSdk = 37

    defaultConfig {
        applicationId = "ru.example.gitsource"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

            buildConfigField("String", "GITHUB_CLIENT_ID", "\"$githubClientIdDev\"")
            buildConfigField("String", "GITHUB_CLIENT_SECRET", "\"$githubClientSecretDev\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "GITHUB_CLIENT_ID", "\"$githubClientIdProd\"")
            buildConfigField("String", "GITHUB_CLIENT_SECRET", "\"$githubClientSecretProd\"")

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.hilt.android)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.google.gson)
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.browser)
    ksp(libs.hilt.compiler)
    debugImplementation(libs.androidx.compose.ui.tooling)
}