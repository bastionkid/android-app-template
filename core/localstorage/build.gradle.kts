plugins {
    id("android.library")
}

android {
    namespace = "com.azuredragon.core.localstorage"

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
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit4)
}