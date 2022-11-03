plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    configure(
        namespace = "com.osh.sample.splash"
    )
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {
    core()
    compose()
    navigation()
    hilt()
    // projects
    implementation(project(":core:features"))
    implementation(project(":core:ui"))

    test()
}
