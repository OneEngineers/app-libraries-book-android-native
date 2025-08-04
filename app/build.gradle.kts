plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") // ✅ required for Kotlin 2.0 + Compose
    id("com.apollographql.apollo") version "4.2.0"
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
}

apollo {
    service("libraries") {
        packageName.set("com.assistant.libraries.graphql")
        introspection {
            endpointUrl.set("http://localhost:84/graphql")
            schemaFile.set(file("src/main/graphql/com/assistant/libraries/schema.graphqls"))
        }
    }
}

dependencies {
    // Compose BOM (optional but cleaner)
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

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
}
