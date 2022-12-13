plugins {
    id("android.library")
}

android {
    namespace = "com.azuredragon.core.data"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.localstorage)
    implementation(projects.core.network)
    implementation(projects.core.time)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.junit4)
}