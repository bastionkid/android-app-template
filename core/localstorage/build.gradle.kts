plugins {
    id("android.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.azuredragon.core.localstorage"

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
    implementation(projects.core.log)
    implementation(projects.core.time)

    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.json)

    api(libs.room.ktx)
//    ksp(libs.room.compiler)

    implementation(libs.koin.core)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.junit4)
}