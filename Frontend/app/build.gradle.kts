plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.cronosdev.taskmanagerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cronosdev.taskmanagerapp"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //
    // JETPACK COMPOSE MATERIAL
    implementation ("androidx.compose.material:material:1.7.6")
    implementation ("androidx.compose.material3:material3:1.3.1")
    implementation ("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation ("androidx.compose.material:material-icons-extended:1.5.4")
    implementation ("androidx.compose.ui:ui:1.7.6")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.6")
    implementation ("androidx.activity:activity-compose:1.9.3")
    implementation ("androidx.core:core-splashscreen:1.0.1")
    // ¿¿??
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    // RETROFIT
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")
    // HILT + Compiler
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")
    //ksp ("com.google.dagger:hilt-compiler:2.48") CAUSA CONFLICTO CON LA ANTERIOR A ESTA
    // FIREBASE
    //implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    //implementation(libs.firebase.firestore.ktx)// Hilt con KSP
    //ksp ("com.google.dagger:dagger-compiler:2.48") // Dagger compiler solo para dagger puro
    // MATERIAL GOOGLE
    implementation("com.google.android.material:material:1.11.0")
    //TESTING
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}