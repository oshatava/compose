plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    configure(
        namespace = "com.osh.sample.main"
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
    retrofit()
    // projects
    implementation(project(":core:features"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))

    test()
}