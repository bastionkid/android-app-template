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

    implementation(projects.core.data)
    implementation(projects.core.log)

    testImplementation(libs.junit4)
}