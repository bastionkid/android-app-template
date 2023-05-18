plugins {
    id("android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.azuredragon.core.analytics"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.localstorage)
    implementation(projects.core.log)
    implementation(projects.core.time)

    implementation(libs.firebase.analytics)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.koin.core)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)

    testImplementation(libs.junit4)
}