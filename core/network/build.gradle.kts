plugins {
    id("android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.azuredragon.core.network"

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
    api(projects.core.log)
    implementation(projects.core.common)

    implementation(libs.kotlinx.coroutines.core)
    api(libs.retrofit.core)
    api(libs.retrofit.kotlin.serialization)
    api(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.cronet)
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.performance)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
    implementation(libs.koin.core)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)

    testImplementation(libs.junit4)
}