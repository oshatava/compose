import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion

object Config {
    const val compileSdk = 33
    const val minSdk = 23
    const val targetSdk = 33
    const val kotlinCompilerExtensionVersion = "1.3.1"
}

fun AppExtension.configure(
    applicationId: String,
    namespace: String,
    versionCode: Int = 1,
    versionName: String = "1.0",
) {
    configureBase(namespace)

    defaultConfig {
        this.applicationId = applicationId
        this.versionCode = versionCode
        this.versionName = versionName
    }
}

fun LibraryExtension.configure(
    namespace: String,
    composeEnabled: Boolean = true,
) {
    configureBase(namespace)
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
        compose = composeEnabled
    }
}

fun BaseExtension.configureBase(
    namespace: String
) {
    this.namespace = namespace
    compileSdkVersion(Config.compileSdk)
    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Config.kotlinCompilerExtensionVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}