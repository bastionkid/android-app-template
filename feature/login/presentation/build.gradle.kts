plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.azuredragon.login.presentation"

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
    implementation(projects.core.ui)
    implementation(projects.feature.login.domain)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.androidx.appcompat)
    implementation(libs.koin.core)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
}