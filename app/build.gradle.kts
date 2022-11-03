plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    configure(
        applicationId = "com.osh.compose",
        namespace = "com.osh.compose",
    )
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

kapt {
    correctErrorTypes = true
}
hilt {
    enableAggregatingTask = true
}

dependencies {
    core()
    compose()
    navigation()
    hilt()
    implementation("com.google.android.material:material:1.7.0")
    implementation(project(":core:ui"))
    implementation(project(":core:features"))
    implementation(project(":features:splash"))
    implementation(project(":features:main"))
}