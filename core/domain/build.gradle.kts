plugins {
    id("android.library")
}

android {
    namespace = "com.azuredragon.core.domain"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.androidx.lifecycle.viewModelCompose)

    testImplementation(libs.junit4)
}