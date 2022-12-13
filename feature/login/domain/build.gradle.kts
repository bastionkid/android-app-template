plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.azuredragon.login.domain"

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
    implementation(projects.core.domain)
    implementation(projects.feature.login.data)

    api(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
}