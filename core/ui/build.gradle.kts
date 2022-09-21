plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.azuredragon.core.ui"

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
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)

    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.material)
//    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.animation)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.ui.tooling.preview)
    debugApi(libs.androidx.compose.ui.tooling)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.flowlayout)

    implementation(libs.coil.kt.compose)

    androidTestImplementation(libs.junit4)
}