plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") // ✅ required for Kotlin 2.0 + Compose
    id("com.apollographql.apollo") version "4.3.2"
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

kapt {
    correctErrorTypes = true
}

android {

    namespace = "com.ones.assistant"
    //noinspection GradleDependency,OldTargetApi
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ones.assistant"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            ndk {
                debugSymbolLevel = "FULL"
            }
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            ndk {
                debugSymbolLevel = "FULL"
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // ✅ matches your compose-ui version
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    lint {
        baseline = file("lint-baseline.xml")
    }

    packaging {
        jniLibs {
            keepDebugSymbols.add("**/libandroidx.graphics.path.so")
            keepDebugSymbols.add("**/libdatastore_shared_counter.so")
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:generateFunctionKeyMetaAnnotations=true",
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:featureFlag=IntrinsicRemember",
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:featureFlag=OptimizeNonSkippingGroups",
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:featureFlag=StrongSkipping"
            )
        )
    }
}

apollo {
    service("ones") {
        packageName.set("com.ones.assistant.graphql")
        srcDir(file("src/main/graphql/com/assistant/libraries"))
        introspection {
            endpointUrl.set("https://book-lms.itedev.online/graphql")
            schemaFile.set(file("src/main/graphql/com/ones/assistant/schema.sdl"))
        }
    }
    service("podcast") {
        packageName.set("com.ones.assistant.graphql.podcast")
        srcDir(file("src/main/graphql/podcast"))
        schemaFile.set(file("src/main/graphql/podcast/schema.graphqls"))
    }
    service("auth") {
        packageName.set("com.ones.assistant.graphql.auth")
        srcDir(file("src/main/graphql/auth"))
        schemaFile.set(file("src/main/graphql/auth/schema.graphqls"))
        mapScalar("Upload", "com.apollographql.apollo.api.DefaultUpload")
    }
}

dependencies {
    // Compose BOM (optional but cleaner)
    implementation(platform(libs.compose.bom))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    // App check
    implementation(libs.firebase.bom)
    implementation(libs.firebase.appcheck.playintegrity)

    // Jetpack Compose
    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.ui)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // UI & support assistant
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

    // MVVM dependencies
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.foundation)

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Audio playback for podcast episodes
    implementation("androidx.media3:media3-exoplayer:1.5.1")
}
