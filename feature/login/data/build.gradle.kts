plugins {
    id("android.library")
}

android {
    namespace = "com.azuredragon.login.data"

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
    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(libs.koin.core)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)

    testImplementation(libs.junit4)
}