plugins {
    id("android.library")
    id("android.library.compose")
    id("kotlin-parcelize")
}

android {
    namespace = "com.azuredragon.core.ui"

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
    implementation(projects.core.data)
    implementation(projects.core.time)

    api(libs.kotlinx.coroutines.core)

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.constraintlayout.compose)
    api(libs.androidx.fragment)
    implementation(libs.androidx.navigation.fragment)
    api(libs.androidx.browser)

    implementation(libs.lottie.compose)
    implementation(libs.rive.android)

    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.material)
//    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.animation)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.ui.tooling.preview)
    debugApi(libs.androidx.compose.ui.tooling)

    api(libs.accompanist.systemuicontroller)
    api(libs.accompanist.flowlayout)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}