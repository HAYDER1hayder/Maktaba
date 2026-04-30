plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.plugin)
}

android {
    namespace = "com.ElOuedUniv.maktaba"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ElOuedUniv.maktaba"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- Supabase & Networking ---
    // نستخدم io.github.jan-tennert للنسخة 2.5.0
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.5.0")
    implementation("io.github.jan-tennert.supabase:storage-kt:2.5.0")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.5.0") // استبدل X.Y.Z بنفس إصدار مكتبات supabase التي تستخدمها


    // Ktor Engines & Serialization
    val ktor_version = "2.3.11"
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // --- UI & Compose ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // --- Dependency Injection (Hilt) ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}