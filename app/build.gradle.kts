plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin") version "2.7.7"
}

android {
    namespace = "com.example.proyectocaravanas_naviajuanma"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyectocaravanas_naviajuanma"
        minSdk = 24
        targetSdk = 35
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
    viewBinding {
        enable = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.compiler)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Stripe
    implementation("com.stripe:stripe-android:21.15.1")

    // JWT Decoder
    implementation("com.auth0.android:jwtdecode:2.0.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    implementation("com.stripe:stripe-android:20.25.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation(platform("com.google.firebase:firebase-bom:31.2.0"))

    implementation("com.google.guava:guava:31.1-android") {
        // Forma correcta de excluir en Kotlin DSL
        exclude(group = "com.google.guava", module = "listenablefuture")
    }
}

// Configuración de constraints correcta para Kotlin DSL
configurations.all {
    resolutionStrategy {
        force("com.google.guava:guava:31.1-android")
    }
}