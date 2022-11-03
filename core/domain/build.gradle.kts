plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    configure(
        namespace = "com.osh.sample.domain",
        composeEnabled = false
    )
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    core()
    test()
}