plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    configure(
        namespace = "com.osh.compose.features"
    )
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    core()
    compose()
    navigation()
    test()
}