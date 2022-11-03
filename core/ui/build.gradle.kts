plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    configure(
        namespace = "com.osh.ui"
    )
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    core()
    compose()
    test()
}