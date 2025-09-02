plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") // ✅ required for Kotlin 2.0 + Compose
    id("com.apollographql.apollo") version "4.3.2"
}

android {
    namespace = "com.assistant.libraries"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.assistant.libraries"
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

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4" // ✅ matches your compose-ui version
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

apollo {
    service("assistant") {
        packageName.set("com.assistant.libraries.graphql")
        introspection {
            endpointUrl.set("https://book-lms.itedev.online/graphql")
            schemaFile.set(file("src/main/graphql/com/assistant/libraries/schema.sdl"))
        }
    }
}

dependencies {
    // Compose BOM (optional but cleaner)
    implementation(platform(libs.compose.bom))

    // App check
    implementation(libs.firebase.bom)
    implementation(libs.firebase.appcheck.playintegrity)



    // Jetpack Compose
    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // UI & support libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)

    // Apollo GraphQL, Retrofit, Hilt
    implementation(libs.apollo.runtime)
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.converter.gson)
    implementation(libs.picasso)
    implementation(libs.hilt.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BoM (manages versions)
    implementation(platform(libs.firebase.bom))

    // App Check Play Integrity (uses version from libs.versions.toml)
    implementation(libs.firebase.appcheck.playintegrity)

    implementation(platform("com.google.firebase:firebase-bom:33.2.0")) // BOM controls versions
    // Firebase dependencies
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    implementation("androidx.compose.material:material-icons-core:1.7.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")

    implementation("androidx.navigation:navigation-compose:2.8.0") // use latest stable

    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.compose.material3:material3:1.3.0")


}
