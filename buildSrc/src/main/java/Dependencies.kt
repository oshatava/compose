import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.core() {
    // core
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
}

fun DependencyHandler.compose() {
    implementation("androidx.activity:activity-compose:1.6.0")
    implementation("androidx.compose.ui:ui:${Versions.composeVersion}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}")
    implementation("androidx.compose.material3:material3:1.0.0-beta03")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("io.coil-kt:coil-compose:2.0.0-rc01")
}

fun DependencyHandler.navigation() {
    implementation("androidx.navigation:navigation-compose:2.5.2")
}

fun DependencyHandler.hilt() {
    implementation("com.google.dagger:hilt-android:${Versions.hiltVersion}")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")
}

fun DependencyHandler.retrofit() {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.7.2")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    kapt("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")
}

fun DependencyHandler.test() {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}


internal fun DependencyHandler.testImplementation(depName: String) {
    add("testImplementation", depName)
}

internal fun DependencyHandler.implementation(depName: String) {
    add("implementation", depName)
}

internal fun DependencyHandler.kapt(depName: String) {
    add("kapt", depName)
}