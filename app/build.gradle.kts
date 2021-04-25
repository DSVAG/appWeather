plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId("com.dsvag.weather")
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)

        buildFeatures {
            viewBinding = true
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    dependencies {
        //Kotlin
        implementation("androidx.core:core-ktx:1.3.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

        //Design
        implementation("androidx.constraintlayout:constraintlayout:2.0.4")
        implementation("com.google.android.material:material:1.3.0")

        //NavigationComponent
        implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
        implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

        //Lifecycle
        implementation("androidx.lifecycle:lifecycle-common:2.4.0-alpha01")

        //Hilt
        implementation("com.google.dagger:hilt-android:2.33-beta")
        implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
        kapt("com.google.dagger:hilt-android-compiler:2.33-beta")
        kapt("androidx.hilt:hilt-compiler:1.0.0-beta01")

        //Coil
        implementation("io.coil-kt:coil:1.2.0")
        implementation("io.coil-kt:coil-svg:1.1.1")

        //Squareup
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
        implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
        implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
        implementation("com.squareup.moshi:moshi-kotlin:1.11.0")

        //Room
        implementation("androidx.room:room-ktx:2.3.0")
        implementation("androidx.room:room-runtime:2.3.0")
        kapt("androidx.room:room-compiler:2.3.0")

        //Google
        implementation("com.google.android.gms:play-services-location:18.0.0")
    }
}